package com.example.lostandfoundbackground.config.security.filter;

import com.example.lostandfoundbackground.config.security.jwt.JwtTokenProvider;
import com.example.lostandfoundbackground.service.impl.MyAdminDetailService;
import com.example.lostandfoundbackground.service.impl.MyUserDetailService;
import com.example.lostandfoundbackground.utils.RedisUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_ADMIN_ACCESS_TOKEN;
import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_USER_ACCESS_TOKEN;

/**
 * @author archi
 */
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    private final MyAdminDetailService adminDetailService;

    private final MyUserDetailService userDetailService;
    public TokenAuthFilter(JwtTokenProvider jwtTokenProvider, MyAdminDetailService adminDetailService, MyUserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminDetailService = adminDetailService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("进入TokenAuthFilter");
        log.info("请求url为: "+ request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        //token为空,允许通过让其登录获取token
        if(ObjectUtils.isEmpty(authHeader) ||!authHeader.startsWith("Bearer ")){
            log.info(getClass().getSimpleName() + ": token为空,允许通过让其登录获取token");
            filterChain.doFilter(request,response);
            return;
        }
        String accessJwt = authHeader.substring(7);
        String userName = jwtTokenProvider.getUserName(accessJwt);
        String userRole = jwtTokenProvider.getUserRole(accessJwt);
        log.info("jwt解析出手机号为:"+ userName);

        if(!ObjectUtils.isEmpty(userName) &&
                SecurityContextHolder.getContext().getAuthentication()==null){
            //根据解析出的username 从数据库获取信息,封装UserDetail对象
            UserDetails userDetails;
            boolean isTokenValid;
            String redisKey;
            if("ADMIN".equals(userRole)){
                userDetails = adminDetailService.loadUserByUsername(userName);
                redisKey = LOGIN_ADMIN_ACCESS_TOKEN;
            }else{
                userDetails = userDetailService.loadUserByUsername(userName);
                redisKey = LOGIN_USER_ACCESS_TOKEN;
            }

            log.info(redisKey+accessJwt);
            isTokenValid = RedisUtils.hasKey(redisKey + accessJwt);
            //如果redis中存有token,那么accessToken未过期,可以访问
            log.info("redis中是否存有token: "+isTokenValid);
            log.info("jwtTokenProvider.validateToken(accessJwt,userDetails): "+jwtTokenProvider.validateToken(accessJwt,userDetails));

            if(jwtTokenProvider.validateToken(accessJwt,userDetails)&&isTokenValid){
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                log.info("当前用户拥有的权限为: "+userDetails.getAuthorities().toString());

                //如果令牌有效,封装一个UsernamePasswordAuthenticationToken对象
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authenticationToken);
                //更新安全上下文的持有用户
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request,response);
    }
}

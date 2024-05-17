package com.example.lostandfoundbackground.config.security.jwt;


import cn.hutool.core.lang.UUID;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityAdminDetails;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.utils.EncryptUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author archi
 */
@Component
@Slf4j
public class JwtTokenProvider {
    //jwt的secret是MD5对salt进行加密
    private final SecretKey secret = Keys.hmacShaKeyFor(EncryptUtil.getMD5String(System.getenv("JWT_KEY")).getBytes());
    private Map<String,Object> initClaims(String username,Long expiration){
        Map<String, Object> claims = new HashMap<>();
        //"iss" (Issuer): 代表 JWT 的签发者。在这个字段中填入一个字符串，表示该 JWT 是由谁签发的。例如，可以填入你的应用程序的名称或标识符。
        claims.put("iss","jx");
        //"sub" (Subject): 代表 JWT 的主题，即该 JWT 所面向的用户。可以是用户的唯一标识符或者其他相关信息。
        claims.put("sub",username);
        //"exp" (Expiration Time): 代表 JWT 的过期时间。通常以 UNIX 时间戳表示，表示在这个时间之后该 JWT 将会过期。建议设定一个未来的时间点以保证 JWT 的有效性，比如一个小时、一天、一个月后的时间。
        claims.put("exp", generateExpirationDate(expiration));
        //"aud" (Audience): 代表 JWT 的接收者。这个字段可以填入该 JWT 预期的接收者，可以是单个用户、一组用户、或者某个服务。
        claims.put("aud","internal use");
        //"iat" (Issued At): 代表 JWT 的签发时间。同样使用 UNIX 时间戳表示。
        claims.put("iat",new Date());
        //"jti" (JWT ID): JWT 的唯一标识符。这个字段可以用来标识 JWT 的唯一性，避免重放攻击等问题。
        claims.put("jti", UUID.randomUUID().toString());
        //"nbf" (Not Before): 代表 JWT 的生效时间。在这个时间之前 JWT 不会生效，通常也是一个 UNIX 时间戳。这里不填，没这个需求
        return claims;
    }

    /**
     * 生成失效时间，以秒为单位
     *
     * @return 预计失效时间
     */

    private Object generateExpirationDate(Long millis) {
        //预计失效时间为：token生成时间+预设期间
        //这里设置成72小时过期 1000 * 60 * 60* 72, 1000ms = 1s
        return new Date(System.currentTimeMillis() + millis);
    }

    /*
        根据账号、角色信息创建token
     */
    /**
     * 根据用户信息生成token
     *
     * @param userName 用户名
     * @return token
     */
    public String generateToken(String userName, String role,Long expiration)
    {
        Map<String, Object> claims = initClaims(role+":"+userName,expiration);
        return generateToken(claims);
    }

    /**
     * 根据负载生成JWT token
     * @param claims 负载
     * @return token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .claims(claims)
                .signWith(secret,Jwts.SIG.HS256)
                .compact();
    }


    /**
     * 从Token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUserName(String token){
        String username;
        try
        {
            username = getPayload(token).getSubject();
            username = username.substring(username.indexOf(":")+1);
        }catch (Exception e){
            username = null;
            log.info(e.getMessage());
        }
        return username;
    }

    /*
     * 从Token中获取用户的角色
     */
    public String getUserRole(String token){
        String role;
        try
        {
            role = getPayload(token).getSubject();
            role = role.substring(0,role.indexOf(":"));
        }catch (Exception e){
            role = null;
            log.info(e.getMessage());
        }
        return role;
    }

    /**
     * 从Token中获取负载中的Claims
     * @param token token
     * @return 负载
     */
    private Claims getPayload(String token)
    {
        try {
            return Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    /**
     * 验证token是否有效
     * @param token 需要被验证的token
     * @param userDetails true/false
     * @return 有效为true,无效false
     */
    public boolean validateToken(String token,SecurityUserDetails userDetails){
        return getUserName(token).equals(userDetails.getUsername()) && isTokenExpired(token);
    }

    public boolean validateToken(String token, SecurityAdminDetails adminDetails){
        return getUserName(token).equals(adminDetails.getUsername()) && isTokenExpired(token);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        if(userDetails instanceof SecurityUserDetails){
           return validateToken(token,(SecurityUserDetails) userDetails);
        }else if(userDetails instanceof SecurityAdminDetails){
            return validateToken(token,(SecurityAdminDetails) userDetails);
        }
        //注意如果不为上面的类型，直接验证失败
        log.info("validateToken出现错误,要验证的类型为: "+userDetails.getClass());
        return false;
    }

    /**
     * 判断token是否有过期
     * @param token 需要被验证的token
     * @return true/false
     */
    private boolean isTokenExpired(String token)
    {
        //判断预设时间是否在当前时间之前，如果在当前时间之前，就表示过期了，会返回true
        return !getExpiredDate(token).before(new Date());
    }

    /**
     * 从token中获取预设的过期时间
     * @param token token
     * @return 预设的过期时间
     */
    private Date getExpiredDate(String token)
    {
        return getPayload(token).getExpiration();
    }

    /**
     * 判断token是否可以被刷新
     * @param token 需要被验证的token
     * @return true/false
     */
    public boolean canRefresh(String token){
        return isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token 需要被刷新的token
     * @return 刷新后的token
     */
    public String refreshToken(String token,Long expiration){
        Claims claims = getPayload(token);
        Map<String, Object> initClaims = initClaims(claims.getSubject(),expiration);
        initClaims.put("iat",new Date());
        return generateToken(initClaims);
    }

    public String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    public Map<String,String> generateTokenMap(String accessToken,String refreshToken){
        return new HashMap<>(){
            {
                put("accessToken",accessToken);
                put("refreshToken",refreshToken);
            }
        };
    }
}

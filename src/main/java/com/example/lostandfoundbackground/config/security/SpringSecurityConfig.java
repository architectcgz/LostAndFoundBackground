package com.example.lostandfoundbackground.config.security;
import com.example.lostandfoundbackground.config.security.authManager.CustomAdminAuthManager;
import com.example.lostandfoundbackground.config.security.authManager.DelegatingAuthManager;
import com.example.lostandfoundbackground.config.security.filter.TokenAuthFilter;
import com.example.lostandfoundbackground.config.security.handlers.CustomAccessDeniedHandler;
import com.example.lostandfoundbackground.config.security.handlers.InvalidAuthEntryPoint;
import com.example.lostandfoundbackground.config.security.jwt.JwtTokenProvider;
import com.example.lostandfoundbackground.config.security.authManager.CustomUserAuthManager;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.mapper.UserMapper;
import com.example.lostandfoundbackground.service.impl.MyAdminDetailService;
import com.example.lostandfoundbackground.service.impl.MyUserDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author archi
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SpringSecurityConfig {

    private final MyUserDetailService myUserDetailService;
    private final MyAdminDetailService myAdminDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final InvalidAuthEntryPoint invalidAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    //不需要登录也能访问的地址
    private final String []WHITE_LIST = {
            "/admin/login",
            "/user/register/**",
            "/user/login",
            "/notification/list",
            "/category/list",
            "/user/refresh_token",
            "/admin/refresh_token",
            "/found/list",
            "/lost/list"
    };
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用basic明文验证
                .httpBasic(httpBasic-> httpBasic.disable())
                // 前后端分离架构不需要csrf保护
                .csrf(csrf-> csrf.disable())
                // 禁用默认登录页
                .formLogin(formLogin-> formLogin.disable())
                // 禁用默认登出页
                .logout(logout-> logout.disable())
                // 前后端分离是无状态的，不需要session了，直接禁用。
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 允许所有OPTIONS请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 允许直接访问授权登录接口
                        .requestMatchers(WHITE_LIST).permitAll()
                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 允许任意请求被已登录用户访问，不检查Authority
                        .anyRequest().authenticated()
                )
                .authenticationProvider(new CustomAuthProvider(myUserDetailService,myAdminDetailService,passwordEncoder()))
                .addFilterBefore(new TokenAuthFilter(jwtTokenProvider,myAdminDetailService,myUserDetailService),
                        UsernamePasswordAuthenticationFilter.class
                )
                // 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
                .exceptionHandling(
                        exceptions->exceptions.authenticationEntryPoint(invalidAuthEntryPoint)
                                .accessDeniedHandler(customAccessDeniedHandler)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        log.info("authenticationManager... start");
        CustomUserAuthManager customUserAuthManager = new CustomUserAuthManager(passwordEncoder(),userMapper);
        CustomAdminAuthManager customAdminAuthManager = new CustomAdminAuthManager(passwordEncoder(),adminMapper);
        return new DelegatingAuthManager(customUserAuthManager,customAdminAuthManager);
    }
}

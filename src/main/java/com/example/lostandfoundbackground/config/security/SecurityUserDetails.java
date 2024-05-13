package com.example.lostandfoundbackground.config.security;

import com.example.lostandfoundbackground.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author archi
 * 接收用户认证鉴权信息的实体
 */
public class SecurityUserDetails implements UserDetails {

    private User user;
    //用户拥有的权限
    private Set<GrantedAuthority>authorities;
    //用户是否被锁定
    private boolean accountNonLocked;
    //用户是否过期
    private boolean accountNonExpired;
    //用户的凭证(通常是密码)是否过期
    private boolean credentialsNonExpired;
    //用户是否被启用
    private boolean enabled;

    public SecurityUserDetails(User user,Collection<? extends GrantedAuthority> authorities,
                               boolean accountNonExpired, boolean accountNonLocked,
                               boolean credentialsNonExpired, boolean enabled){
        this.user = user;
        this.authorities = authorities.stream().map(authority ->
                new SimpleGrantedAuthority(authority.getAuthority())
        ).collect(Collectors.toUnmodifiableSet());
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

package com.example.lostandfoundbackground.config.security.userDetails;

import com.example.lostandfoundbackground.entity.Admin;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author archi
 */
@Data
public class SecurityAdminDetails implements UserDetails {
    private Admin admin;

    //用户拥有的权限
    private Set<GrantedAuthority> authorities;
    //用户是否被锁定
    private boolean accountNonLocked;
    //用户是否过期
    private boolean accountNonExpired;
    //用户的凭证(通常是密码)是否过期
    private boolean credentialsNonExpired;
    //用户是否被启用
    private boolean enabled;

    public SecurityAdminDetails(Admin admin, Collection<? extends GrantedAuthority> authorities,
                               boolean accountNonExpired, boolean accountNonLocked,
                               boolean credentialsNonExpired, boolean enabled){
        this.admin = admin;
        this.authorities = authorities.stream().map(authority ->
                new SimpleGrantedAuthority(authority.getAuthority())
        ).collect(Collectors.toUnmodifiableSet());
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = admin.getStatus()!=-1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getPhone();
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

package com.example.lostandfoundbackground.config.security.authManager;

import com.example.lostandfoundbackground.config.security.authManager.AbstractCustomAuthManager;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_ADMIN_PHONE;

/**
 * @author archi
 */
@Slf4j
public class CustomAdminAuthManager extends AbstractCustomAuthManager {
    private final AdminMapper adminMapper;

    public CustomAdminAuthManager(PasswordEncoder passwordEncoder, AdminMapper adminMapper) {
        super(passwordEncoder);
        this.adminMapper = adminMapper;
    }

    @Override
    protected Object findUserByPhone(String phone) {
        return adminMapper.findAdminBaseByPhone(phone);
    }

    @Override
    protected Class<?> getUserClass() {
        return Admin.class;
    }

    @Override
    protected String getLoginKey() {
        return LOGIN_ADMIN_PHONE;
    }

    @Override
    protected String getPassword(Object user) {
        return ((Admin)user).getPassword();
    }

    @Override
    protected String getRole() {
        return "ROLE_ADMIN";
    }
}

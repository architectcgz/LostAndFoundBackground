package com.example.lostandfoundbackground.config.security.authManager;

import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_USER_PHONE;

/**
 * @author archi
 */
@Slf4j
public class CustomUserAuthManager extends AbstractCustomAuthManager {
    private final UserMapper userMapper;

    public CustomUserAuthManager(PasswordEncoder passwordEncoder,UserMapper userMapper) {
        super(passwordEncoder);
        this.userMapper = userMapper;
    }

    @Override
    protected Object findUserByPhone(String phone) {
        return userMapper.findUserBaseByPhone(phone);
    }

    @Override
    protected Class<?> getUserClass() {
        return User.class;
    }

    @Override
    protected String getLoginKey() {
        return LOGIN_USER_PHONE;
    }

    @Override
    protected String getPassword(Object user) {
        return ((User)user).getPassword();
    }

    @Override
    protected String getRole() {
        return "ROLE_USER";
    }
}
package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.RegisterFormDTO;
import com.example.lostandfoundbackground.dto.Result;

/**
 * @author archi
 */
public interface UserService {
    Result login(LoginFormDTO loginForm);

    Result logout(String token);

    Result sendSmsCode();

    Result sendSmsCodeToRegister(String phone);

    Result validateSmsCode(String code, String token);

    Result modifyPwd(String token, ChangePwdDTO changePwdDTO);

    Result register(RegisterFormDTO registerFormDTO);

    Result validateSmsCodeToRegister(String phone,String code);

    Result getUserInfo();

    Result updateAvatar(String avatarUrl);

    Result updateUserName(String newName);
}

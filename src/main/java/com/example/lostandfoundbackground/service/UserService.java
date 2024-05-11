package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.RegisterFormDTO;
import com.example.lostandfoundbackground.dto.Result;

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
}

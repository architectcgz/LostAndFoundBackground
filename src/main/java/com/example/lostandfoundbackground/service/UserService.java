package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.*;

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

    Result refreshToken(String accessToken,String refreshToken);

    Result forgetPwd(ForgetPwdDTO forgetPwdDto);

    Result getMyLostInfo(int pageNum,int pageSize);

    Result getMyFoundInfo(int pageNum, int pageSize);
}

package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;


/**
 * @author archi
 */
public interface AdminService {
    Result login(LoginFormDTO loginForm);
    Result logout(String token);
    Result addAdmin(Admin admin);
    Result deleteAdmin(Long id);
    Result sendSmsCode();

    Result validateSmsCode(String code,String token);

    Result modifyPwd(String token, ChangePwdDTO changePwdDTO);

    Result addNotification(NotificationDTO notificationDTO);

    Result banAdmin(Long id);

    Result deleteNotification(Long id);
}

package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.NewsDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;


/**
 * @author archi
 */
public interface AdminService {
    public Result login(LoginFormDTO loginForm);
    public Result logout(String token);
    public Result addAdmin(Admin admin);
    public Result deleteAdmin(Long id);
    public Result sendSmsCode();

    public Result validateSmsCode(String code);

    public Result changePwd(String token,ChangePwdDTO changePwdDTO);

    public Result addNews(NewsDTO newsDTO);

    public Result banAdmin(Long id);
}

package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;


public interface AdminService {
    public Result login(LoginFormDTO loginForm);
    public Result logout(String token);

    public Result addAdmin(Admin admin);
}

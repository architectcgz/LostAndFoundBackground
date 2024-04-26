package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import jakarta.servlet.http.HttpSession;

public interface AdminService {
    public Result login(LoginFormDTO loginForm, HttpSession session);
}

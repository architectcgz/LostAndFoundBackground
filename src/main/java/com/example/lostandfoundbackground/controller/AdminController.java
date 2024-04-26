package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.AdminService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author archi
 */
@RequestMapping("/admin")
@RestController
@Validated
public class AdminController {
    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        return adminService.login(loginForm,session);
    }
}

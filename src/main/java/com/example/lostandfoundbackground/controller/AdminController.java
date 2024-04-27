package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.AdminService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@Slf4j
@RequestMapping("/admin")
@RestController
@Validated
public class AdminController {
    @Resource
    private AdminService adminService;

    //这里前端要传递Json数据
    /*
    下面的数据只用于演示，不正确
        {
            "phone": "112624",
            "password": "asd"
        }
     */
    @PostMapping("/login")
    Result login(@RequestBody LoginFormDTO loginForm){
        return adminService.login(loginForm);
    }

    @PostMapping("/logout")
    Result logout(@RequestHeader("Authorization")String token){
        log.info(token);
        return adminService.logout(token);
    }
}

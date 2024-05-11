package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.service.AdminService;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
    @PostMapping("/login")
    Result login(@RequestBody LoginFormDTO loginForm){
        return adminService.login(loginForm);
    }

    @PostMapping("/logout")
    Result logout(@RequestHeader("Authorization")String token){
        return adminService.logout(token);
    }
    /*
        添加新的管理员
     */

    @PostMapping("/add")
    Result addAdmin(@RequestBody Admin admin){
        return adminService.addAdmin(admin);
    }

    @PostMapping("/delete")
    Result deleteAdmin(@RequestParam("id")Long id ){
        return adminService.deleteAdmin(id);
    }

    @PostMapping("/ban")
    Result banAdmin(@RequestParam("id")Long id){
        return adminService.banAdmin(id);
    }

    @PostMapping("/code")
    Result sendSmsCode(){
        return adminService.sendSmsCode();
    }

    @PostMapping("/validateSmsCode")
    @ApiOperation("验证短信code是否正确")
    public Result validateSmsCode(@RequestParam("code")String code,@RequestHeader("Authorization")String token){
        return adminService.validateSmsCode(code,token);
    }

    @PostMapping("/modifyPwd")
    Result modifyPwd(@RequestHeader("Authorization")String token, @RequestBody ChangePwdDTO changePwdDTO){
        return adminService.modifyPwd(token,changePwdDTO);
    }

    @PostMapping("/notification/publish")
    Result addNotification(@RequestBody NotificationDTO notificationDTO){
        return adminService.addNotification(notificationDTO);
    }

    @PostMapping("/notification/delete")
    Result deleteNotification(@RequestParam Long id){
        return adminService.deleteNotification(id);
    }
}

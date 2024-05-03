package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
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
        return adminService.logout(token);
    }
    /*
        添加新的管理员
     */

    @PostMapping("/add")
    Result addAdmin(@RequestBody Admin admin){
        return adminService.addAdmin(admin);
    }

    @PostMapping("/code")
    Result sendSmsCode(){
        return adminService.sendSmsCode();
    }

    @PostMapping("/validateSmsCode")
    @ApiOperation("验证短信code是否正确")
    public Result validateSmsCode(@RequestParam("code")String code){
        return adminService.validateSmsCode(code);
    }

    @PostMapping("/changePwd")
    Result changePwd(@RequestHeader("Authorization")String token,@RequestBody ChangePwdDTO changePwdDTO){
        return adminService.changePwd(token,changePwdDTO);
    }
}

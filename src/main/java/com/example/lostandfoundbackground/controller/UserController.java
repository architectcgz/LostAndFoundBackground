package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.*;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.service.UserService;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    Result login(@RequestBody LoginFormDTO loginForm){
        return userService.login(loginForm);
    }

    @PostMapping("/pwd/forget")
    Result getBackPwd(@RequestBody ForgetPwdDTO forgetPwdDto){
        return userService.forgetPwd(forgetPwdDto);
    }
    @PostMapping("/logout")
    Result logout(@RequestHeader("Authorization")String token){
        return userService.logout(token);
    }

    @PostMapping("/refresh_token")
    Result refreshToken(@RequestHeader("Authorization")String accessToken,@RequestHeader("RefreshToken")String refreshToken){
        return userService.refreshToken(accessToken,refreshToken);
    }

    @GetMapping("/code")
    Result sendSmsCode(){
        return userService.sendSmsCode();
    }

    @GetMapping("/register/code")
    Result sendSmsCodeToRegister(@RequestParam("phone")String phone){
        return userService.sendSmsCodeToRegister(phone);
    }

    @GetMapping("/register/code/validate")
    @ApiOperation("验证短信code是否正确")
    public Result validateSmsCodeToRegister(@RequestParam("phone")String phone,@RequestParam("code")String code){
        return userService.validateSmsCodeToRegister(phone,code);
    }


    @GetMapping("/code/validate")
    @ApiOperation("验证短信code是否正确")
    public Result validateSmsCode(@RequestParam("code")String code,@RequestHeader("Authorization")String token){
        return userService.validateSmsCode(code,token);
    }

    @PostMapping("/pwd/modify")
    Result modifyPwd(@RequestHeader("Authorization")String token, @RequestBody ChangePwdDTO changePwdDTO){
        return userService.modifyPwd(token,changePwdDTO);
    }

    @PostMapping("/register")
    Result register(@RequestBody RegisterFormDTO registerFormDTO){
        return userService.register(registerFormDTO);
    }

    @GetMapping("/info")
    Result getUserInfo(){
        return userService.getUserInfo();
    }

    //要求传递来的头像必须是一个URL地址
    @PostMapping("/avatar/update")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        return userService.updateAvatar(avatarUrl);
    }

    @PostMapping("/name/update")
    public Result updateUserName(@RequestParam String newName){
        return userService.updateUserName(newName);
    }

    @GetMapping("/my-lost")
    public Result getMyLostItems(@RequestParam int pageNum,@RequestParam int pageSize){
        return userService.getMyLostInfo(pageNum,pageSize);
    }

    @GetMapping("/my-found")
    public Result getMyFoundItems(@RequestParam int pageNum,@RequestParam int pageSize){
        return userService.getMyFoundInfo(pageNum,pageSize);
    }
}

package com.example.lostandfoundbackground.utils;

import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.RegisterFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.ADMIN_SMS_CODE_KEY;

/**
 * @author archi
 */
@Slf4j
public class ValidateUtils {
    /*
    * 验证登录表单中的数据与是否正确
    * 正确返回true,错误返回false
     */
    public static Boolean validateLoginForm(LoginFormDTO loginFormDTO){
        String phone = loginFormDTO.getPhone();
        String password = loginFormDTO.getPassword();
        if(!(RegexUtils.isPhoneValid(phone)&&RegexUtils.isPasswordValid(password))){
            //不符合手机号的格式，返回错误信息
            log.info("手机号是否符合格式:"+ RegexUtils.isPhoneValid(phone));
            log.info("密码是否符合格式"+ RegexUtils.isPasswordValid(password));
            return false;
        }
        return true;
    }

    /*
        验证
     */
    public static Map<String,Object> validateRegisterForm(RegisterFormDTO registerFormDTO){
        String phone = registerFormDTO.getPhone();
        String pwd = registerFormDTO.getPwd();
        String repeatPwd = registerFormDTO.getRepeatPwd();
        String name = registerFormDTO.getName();
        if(!(RegexUtils.isPhoneValid(phone)&&RegexUtils.isPasswordValid(pwd))){
            //不符合手机号的格式，返回错误信息
            log.info("手机号是否符合格式:"+ RegexUtils.isPhoneValid(phone));
            log.info("密码是否符合格式"+ RegexUtils.isPasswordValid(pwd));

            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","手机号或密码格式错误!");
                }
            };
        }
        if(!RegexUtils.isUserNameValid(name)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","用户名不符合要求!");
                }
            };
        }
        if(!pwd.equals(repeatPwd)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","两次输入的密码不同,请检查密码");
                }
            };
        }
        return new HashMap<>(){
            {
                put("valid",true);
            }
        };

    }

    public static Result validateSmsCode(String key,String phone,String code){
        String s = key +phone;
        if(!RedisUtils.hasKey(s)){
            return Result.error(1,"该验证码不存在!");
        }
        Map<Object,Object> codeMap = RedisUtils.hmget(s);
        String codeInRedis = (String) codeMap.get("code");
        log.info("在Redis中保存的验证码是:"+codeInRedis);
        if (codeInRedis == null || !codeInRedis.equals(code)) {
            return Result.error(1,"输入的验证码错误或已过期");
        }
        //验证码校验成功后，修改密码的接口开放5min，保证安全性
        //校验过后,设置验证码中verified为true
        RedisUtils.hset(s,"verified","true");
        RedisUtils.expire(s,5L);
        log.info("校验验证码的线程:"+Thread.currentThread().getName());
        return Result.ok("验证码验证成功");
    }
}

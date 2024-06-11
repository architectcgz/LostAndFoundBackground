package com.example.lostandfoundbackground.utils;

import com.example.lostandfoundbackground.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.USER_SMS_CODE_KEY;

/**
 * @author archi
 */
@Slf4j
public class ValidateUtils {
    public static Map<String,Object> validateForgetPwdForm(ForgetPwdDTO forgetPwdDTO){
        String phone = forgetPwdDTO.getPhone();
        String code = forgetPwdDTO.getCode();//验证码
        String pwd = forgetPwdDTO.getPwd();
        String repeatPwd = forgetPwdDTO.getRepeatPwd();
        if(!RegexUtils.isPhoneValid(phone)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","手机号码格式不正确");
                }
            };
        }
        if(!RegexUtils.isPasswordValid(pwd)||!RegexUtils.isPasswordValid(repeatPwd)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","密码格式不正确!");
                }
            };
        }
        if(!pwd.equals(repeatPwd)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","两次输入的密码不同!");
                }
            };
        };
        //校验验证码
        String s = USER_SMS_CODE_KEY +phone;
        if(!RedisUtils.hasKey(s)){
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","验证码不存在");
                }
            };
        }
        Map<Object,Object> codeMap = RedisUtils.hmget(s);
        String codeInRedis = (String) codeMap.get("code");
        log.info("在Redis中保存的验证码是:"+codeInRedis);
        if (codeInRedis == null || !codeInRedis.equals(code)) {
            return new HashMap<>(){
                {
                    put("valid",false);
                    put("msg","输入的验证码错误或已过期");
                }
            };
        }

        //验证码校验成功后，修改密码的接口开放5min，保证安全性
        //校验过后,设置验证码中verified为true
        RedisUtils.hset(s,"verified","true");
        RedisUtils.expire(s,5L);
        log.info("校验验证码的线程:"+Thread.currentThread().getName());
        return new HashMap<>(){
            {
                put("valid",true);
            }
        };

    }
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

    public static Map<String,Object> validateNotificationForm(NotificationDTO notificationDTO){
        String title = notificationDTO.getTitle();
        String description = notificationDTO.getDescription();
        String content = notificationDTO.getContent();
        Map<String,Object>resultMap = new HashMap<>();
        //标题和内容不能为空,通知的描述可以为空
        if(ObjectUtils.isEmpty(title)|| ObjectUtils.isEmpty(content)){
            resultMap.put("valid",false);
            resultMap.put("msg","标题或内容不能为空!");
            return resultMap;
        }
        if(title.length()>20||description.length()>50||content.length()>2000){
            resultMap.put("valid",false);
            resultMap.put("msg","标题、描述和内容字数不得超出限制");
            return resultMap;
        }
        resultMap.put("valid",true);
        return resultMap;
    }

    public static Map<String,Object> validateCategoryDTO(CategoryDTO categoryDTO){
        String categoryName = categoryDTO.getCategoryName();
        String categoryAlias = categoryDTO.getCategoryAlias();
        Map<String,Object> resultMap = new HashMap<>();
        if(ObjectUtils.isEmpty(categoryName)||ObjectUtils.isEmpty(categoryAlias)){
            resultMap.put("valid",false);
            resultMap.put("msg","分类的名称或者别名不能为空!");
            return resultMap;
        }
        if(categoryName.length()>10||categoryAlias.length()>10){
            resultMap.put("valid",false);
            resultMap.put("msg","分类的名称或别名最多10个字符");
            return resultMap;
        }
        resultMap.put("valid",true);
        return resultMap;
    }

}

package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.*;
import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.UserMapper;
import com.example.lostandfoundbackground.service.UserService;
import com.example.lostandfoundbackground.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;

/**
 * @author archi
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Result register(RegisterFormDTO registerForm){
        //校验用户名、密码和手机号是否符合格式
        Map<String, Object>validateResult = ValidateUtils.validateRegisterForm(registerForm);
        Boolean isValid = (Boolean) validateResult.get("valid");
        if(!isValid){
            return Result.error(1, (String) validateResult.get("msg"));
        }
        //校验验证码
        Map<Object,Object>result = RedisUtils.hmget(USER_SMS_CODE_KEY+registerForm.getPhone());
        String smsCode = (String) result.get("code");
        if(smsCode==null||!result.get("code").equals(registerForm.getCode())){
            return Result.error(1,"验证码不存在或已过期!");
        }

        //上面都校验成功后,把用户信息同时存放到redis和mysql
        User user = new User();
        user.setPhone(registerForm.getPhone());
        user.setAvatar("https://big-event-arc.oss-cn-hangzhou.aliyuncs.com/identicon.png");
        user.setName(RandomUtil.randomString(10));
        user.setStatus(0);
        user.setPassword(EncryptUtil.getMD5String(registerForm.getRepeatPwd()));
        user.setCreateTime(DateTime.now());
        user.setUpdateTime(DateTime.now());
        //删除验证码，防止多次注册
        RedisUtils.del(USER_SMS_CODE_KEY+user.getPhone());
        //将注册好的用户信息先保存到数据库中
        try {
            userMapper.addUser(user);
        }catch (Exception e){
            return Result.error(1,"数据保存错误!请联系管理员");
        }
        //如果数据库中保存成功再将信息保存到redis中
        RedisUtils.storeBeanAsJson(user,LOGIN_USER_PHONE+user.getPhone(),LOGIN_USER_TTL);
        return Result.ok();
    }

    @Override
    public Result validateSmsCodeToRegister(String phone,String code) {
        if(!RegexUtils.isPhoneValid(phone)){
            return Result.error(1,"手机号格式有误!");
        }
        return ValidateUtils.validateSmsCode(USER_SMS_CODE_KEY,phone,code);
    }

    @Override
    public Result getUserInfo() {
        UserDTO nowUser = ThreadLocalUtil.get();
        return Result.ok(nowUser);
    }

    @Override
    public Result updateAvatar(String avatarUrl) {
        //TODO 修改可以直接修改缓存中的内容，之后考虑使用MQ
        UserDTO nowUser = ThreadLocalUtil.get();
        //更新数据库中的信息
        userMapper.updateAvatar(nowUser.getId(),avatarUrl);
        //更新redis中的数据
        JSONObject jsonObject = JsonUtils.stringToJsonObj(RedisUtils.get(LOGIN_USER_PHONE+nowUser.getPhone()));
        String token = (String) jsonObject.get("token");
        jsonObject.replace("avatar",avatarUrl);

        RedisUtils.set(LOGIN_USER_PHONE+nowUser.getPhone(),jsonObject.toString());

        RedisUtils.hset(LOGIN_USER_KEY+token,"avatar",avatarUrl);
        return Result.ok();
    }

    @Override
    public Result updateUserName(String newName) {
        //TODO 修改可以直接修改缓存中的内容，之后考虑使用MQ
        UserDTO nowUser = ThreadLocalUtil.get();
        if(newName.equals(nowUser.getName())){
            return Result.error(1,"新用户名与旧用户名相同!");
        }

        if(!RegexUtils.isUserNameValid(newName)){
            return Result.error(1,"用户名格式不正确!");
        }

        //更新数据库中的信息
        userMapper.updateUserName(nowUser.getId(),newName);
        //更新redis中的数据
        JSONObject jsonObject = JsonUtils.stringToJsonObj(RedisUtils.get(LOGIN_USER_PHONE+nowUser.getPhone()));
        String token = (String) jsonObject.get("token");
        jsonObject.replace("avatar",newName);

        RedisUtils.set(LOGIN_USER_PHONE+nowUser.getPhone(),jsonObject.toString());

        RedisUtils.hset(LOGIN_USER_KEY+token,"name",newName);
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
        //校验手机号和密码
        String phone = loginForm.getPhone();
        String password = loginForm.getPassword();
        if(!ValidateUtils.validateLoginForm(loginForm)){
            return Result.error(1,"手机号码或密码格式错误");
        }
        //得到加密后的字符串
        String encryptedPwd = EncryptUtil.getMD5String(password);
        //从redis中尝试查询user，不存在再去MySql查询
        User user = null;
        String key = LOGIN_USER_PHONE + phone;
        try {
            if(RedisUtils.hasKey(key)){
                String jsonUser = RedisUtils.get(key);
                //将json反序列化为administrator类型
                user = JsonUtils.jsonToJavaBean(jsonUser, User.class);
                //在其他地点以及登录，删除上次登录的token，挤掉上次的登录
                String oldToken = user.getToken();
                if(oldToken!=null){
                    RedisUtils.del(LOGIN_USER_KEY+oldToken);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(1,e.getMessage());
        }

        //redis中查询不到,从mysql中查询
        if(user == null){
            user = userMapper.findUserBaseByPhone(phone);
            if(user == null){
                return Result.error(1,"用户不存在");
            }
        }

        //前端传来的加密后的密码与实际密码不同，密码错误
        if(!user.getPassword().equals(encryptedPwd)){
            return Result.error(1,"密码错误,请检查");
        }
        //到这里，用户存在且密码正确，发放令牌
        //UUID.randomUUID().toString(true) true参数表示UUID中不含有'-'
        String token = UUID.randomUUID().toString(true);

        //将User转化为HashMap
        //这里只需要用到一些关键数据：id、phone、name,level,不需要使用createTime和updateTime
        //所以使用userDTO接收
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

        //设置token有效期 一天失效
        RedisUtils.storeBeanAsHash(userDTO,LOGIN_USER_KEY + token,1440);

        user.setToken(token);
        //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
        //设置3天失效3*24*60
        RedisUtils.storeBeanAsJson(user,LOGIN_USER_PHONE + phone,4320);

        //返回token
        return Result.ok(token);
    }

    @Override
    public Result logout(String token) {
        if(!StringUtils.hasLength(token)){
            return Result.error(HttpStatus.UNAUTHORIZED,"用户未登录!");
        }
        RedisUtils.del(LOGIN_USER_KEY+token);
        return Result.ok();
    }

    @Override
    public Result sendSmsCode() {
        //从ThreadLocal中获取到当前的用户
        UserDTO nowUser = ThreadLocalUtil.get();
        String phone = nowUser.getPhone();
        //发送验证码
        String smsCode = RandomUtil.randomNumbers(6);
        if(!AliYunSmsUtil.sendSmsAndSave(USER_SMS_CODE_KEY,phone,smsCode)){
            return Result.error(1,"发送验证码失败");
        }
        return Result.ok();
    }

    @Override
    public Result sendSmsCodeToRegister(String phone) {
        if(!RegexUtils.isPhoneValid(phone)){
            return Result.error(1,"手机号格式不正确!");
        }
        String smsCode = RandomUtil.randomNumbers(6);
        if(!AliYunSmsUtil.sendSmsAndSave(USER_SMS_CODE_KEY,phone,smsCode)){
            return Result.error(1,"发送验证码失败");
        }
        return Result.ok();
    }

    @Override
    public Result validateSmsCode(String code, String token) {
        //从ThreadLocal中获取到当前的用户
        UserDTO nowUser = ThreadLocalUtil.get();
        String phone = nowUser.getPhone();
        return ValidateUtils.validateSmsCode(USER_SMS_CODE_KEY,phone,code);
    }

    @Override
    public Result modifyPwd(String token, ChangePwdDTO changePwdDTO) {
        //从ThreadLocal中获取到当前的管理员
        UserDTO nowUser = ThreadLocalUtil.get();
        if(nowUser==null){
            return Result.error(1,"请检查账号登录状态");
        }
        if(!changePwdDTO.getNewPwd().equals(changePwdDTO.getRepeatPwd())){
            return Result.error(1,"两次输入的密码不相同,请检查");
        }
        //先尝试修改密码，然后将当前Redis中存放的管理员信息删除(包括token和登录信息)
        String newPwd = EncryptUtil.getMD5String(changePwdDTO.getRepeatPwd());
        //对比新密码与旧密码是否相同，如果相同，那么不修改，减少数据库操作
        String oldPwd = (String) RedisUtils.hget(LOGIN_USER_KEY+token,"password");
        if(newPwd.equals(oldPwd)){
            return Result.error(1,"新密码与旧密码相同!");
        }
        //新旧密码不相同再修改
        userMapper.changePwd(nowUser.getId(),newPwd);
        //从redis中删除token和登录信息
        RedisUtils.del(LOGIN_USER_KEY+token);
        RedisUtils.del(LOGIN_USER_PHONE+nowUser.getPhone());
        //修改密码后重新设置成不允许修改密码,所以删除redis中上次的验证码
        //再次验证验证码正确后再允许修改密码
        RedisUtils.del(USER_SMS_CODE_KEY +nowUser.getPhone());
        return Result.ok();
    }
}

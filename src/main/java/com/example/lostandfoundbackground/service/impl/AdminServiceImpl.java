package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.example.lostandfoundbackground.config.security.jwt.JwtTokenProvider;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityAdminDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.dto.ChangePwdDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.service.AdminService;
import com.example.lostandfoundbackground.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;
import static com.example.lostandfoundbackground.constants.SystemConstants.*;

/**
 * @author archi
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Result login(LoginFormDTO loginForm) {
        //校验手机号和密码
        String phone = loginForm.getPhone();
        String password = loginForm.getPassword();
        if(!ValidateUtils.validateLoginForm(loginForm)){
            return Result.error(1,"手机号码或密码格式错误");
        }
        log.info("开始验证Admin: "+phone+" 的密码是否正确");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        phone,password
                )
        );
        log.info("密码是否正确验证完成");

        //从redis中尝试查询Admin，不存在再去MySql查询
        Admin admin = null;
        String key = LOGIN_ADMIN_PHONE + phone;
        try {
            if(RedisUtils.hasKey(key)){
                String jsonAdmin = RedisUtils.get(key);
                //将json反序列化为administrator类型
                admin = JsonUtils.jsonStrToJavaBean(jsonAdmin,Admin.class);

                //在其他地点以及登录，删除上次登录的token，挤掉上次的登录
                String oldRefreshToken = admin.getRefreshToken();
                String oldAccessToken = (String)RedisUtils.hget(LOGIN_ADMIN_REFRESH_TOKEN+oldRefreshToken,"accessToken");
                log.info("OldRefreshToken: "+oldRefreshToken);
                log.info("OldAccessToken: "+oldAccessToken);
                RedisUtils.del(LOGIN_ADMIN_ACCESS_TOKEN+oldAccessToken);
                RedisUtils.del(LOGIN_ADMIN_REFRESH_TOKEN+oldRefreshToken);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(1,e.getMessage());
        }

        //redis中查询不到,从mysql中查询
        if(admin == null){
            admin = adminMapper.findAdminBaseByPhone(phone);
            if(admin == null){
                return Result.error(1,"该管理员不存在");
            }
            if(admin.getStatus()==-1){
                return Result.error(1,"该管理员已被禁用");
            }
        }
        String refreshToken = jwtTokenProvider.generateToken(admin.getPhone(),ROLE_ADMIN,REFRESH_TOKEN_EXPIRATION);
        String accessToken = jwtTokenProvider.generateToken(admin.getPhone(),ROLE_ADMIN,ACCESS_TOKEN_EXPIRATION);
        admin.setRefreshToken(refreshToken);

        Map<String,String> tokenMap = jwtTokenProvider.generateTokenMap(accessToken,refreshToken);

        //将Administrator转化为HashMap
        //这里只需要用到一些关键数据：id、phone、name,level,不需要使用createTime和updateTime
        //所以使用adminDTO接收
        AdminDTO adminDTO = BeanUtil.copyProperties(admin, AdminDTO.class);
        adminDTO.setAccessToken(accessToken);
        //设置refreshToken有效期 天失效
        RedisUtils.storeBeanAsHash(adminDTO, LOGIN_ADMIN_REFRESH_TOKEN + refreshToken,REDIS_THREE_DAYS_EXPIRATION);
        //设置accessToken 20分钟过期
        RedisUtils.set(LOGIN_ADMIN_ACCESS_TOKEN+accessToken,accessToken,20L);
        //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
        //设置7天失效
        RedisUtils.storeBeanAsJson(admin,LOGIN_ADMIN_PHONE + phone,REDIS_ONE_WEEK_EXPIRATION);

        //返回token
        return Result.ok(tokenMap);
    }

    /*
        登出需要清除Redis中保存的token
     */
    @Override
    public Result logout(String token) {
        if(!StringUtils.hasLength(token)){
            return Result.error(HttpStatus.UNAUTHORIZED,"用户未登录!");
        }
        RedisUtils.del(LOGIN_ADMIN_REFRESH_TOKEN +token);
        return Result.ok();
    }

    @Override
    public Result addAdmin(Admin admin) {
        //从ThreadLocal中获取到当前的管理员
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = adminDetails.getAdmin();
        //如果当前的管理员不存在，则说明管理员登录状态失效
        if(nowAdmin == null){
            return Result.error(HttpStatus.UNAUTHORIZED,"添加失败,请检查登录状态!");
        }
        //等级为100的为超级管理员，只有超级管理员能添加新的管理员
        if(nowAdmin.getLevel()<100){
            return Result.error(1,"管理员权限不足,不允许添加新的管理员!");
        }
        String password = admin.getPassword();
        String phone = admin.getPhone();
        //验证新添加的Admin手机号与密码是否符合正确的格式
        if(!(RegexUtils.isPhoneValid(phone)&&RegexUtils.isPasswordValid(password))){
            log.info("手机号是否符合格式:"+ RegexUtils.isPhoneValid(phone));
            log.info("密码是否符合格式"+ RegexUtils.isPasswordValid(password));
            return Result.error(1,"手机号码或密码格式错误");
        }
        //设置新管理员的创建时间和更新时间
        admin.setCreateTime(DateTime.now());
        admin.setUpdateTime(DateTime.now());
        //超级管理员添加的是普通管理员
        admin.setLevel(1);
        admin.setPassword(EncryptUtil.getMD5String(password));
        adminMapper.addAdmin(admin);
        return Result.ok();
    }

    @Override
    public Result banAdmin(Long id) {
        //从ThreadLocal中获取到当前的管理员
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = adminDetails.getAdmin();
        //如果当前的管理员不存在，则说明管理员登录状态失效
        if(nowAdmin == null){
            return Result.error(HttpStatus.UNAUTHORIZED,"添加失败,请检查登录状态!");
        }
        if(nowAdmin.getLevel()<100){
            return Result.error(1,"管理员权限不足,不允许禁用其他管理员!");
        }
        adminMapper.banAdminById(id);
        return Result.ok();
    }

    @Override
    public Result deleteAdmin(Long id) {
        //从ThreadLocal中获取到当前的管理员
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        if(nowAdmin.getLevel()<100){
            return Result.error(1,"管理员权限不足,不允许删除其他管理员!");
        }
        adminMapper.deleteAdminById(id);
        return Result.ok();
    }

    @Override
    public Result sendSmsCode() {
        //从ThreadLocal中获取到当前的管理员
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = adminDetails.getAdmin();
        //如果当前的管理员不存在，则说明管理员登录状态失效
        if(nowAdmin == null){
            return Result.error(HttpStatus.UNAUTHORIZED,"添加失败,请检查登录状态!");
        }
        String phone = nowAdmin.getPhone();
        log.info(phone);
        if(!RegexUtils.isPhoneValid(phone)){
            return Result.error(1,"手机号格式不正确!");
        }
        //发送验证码
        String smsCode = RandomUtil.randomNumbers(6);
        if(!AliYunSmsUtil.sendSmsAndSave(ADMIN_SMS_CODE_KEY,phone,smsCode)){
            return Result.error(1,"发送验证码失败");
        }
        return Result.ok();
    }

    @Override
    public Result validateSmsCode(String code,String token) {
        //从ThreadLocal中获取到当前的管理员
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = adminDetails.getAdmin();
        //如果当前的管理员不存在，则说明管理员登录状态失效
        if(nowAdmin == null){
            return Result.error(HttpStatus.UNAUTHORIZED,"添加失败,请检查登录状态!");
        }
        String phone = nowAdmin.getPhone();
        return ValidateUtils.validateSmsCode(ADMIN_SMS_CODE_KEY,phone,code);
    }

    @Override
    public Result modifyPwd(String token, ChangePwdDTO changePwdDTO) {
        //从ThreadLocal中获取到当前的管理员
        SecurityAdminDetails adminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = adminDetails.getAdmin();
        //如果当前的管理员不存在，则说明管理员登录状态失效
        if(nowAdmin == null){
            return Result.error(HttpStatus.UNAUTHORIZED,"添加失败,请检查登录状态!");
        }

        if(!changePwdDTO.getNewPwd().equals(changePwdDTO.getRepeatPwd())){
            return Result.error(1,"两次输入的密码不相同,请检查");
        }

        //先尝试修改密码，然后将当前Redis中存放的管理员信息删除(包括token和登录信息)
        String newPwd = passwordEncoder.encode(changePwdDTO.getRepeatPwd());
        //对比新密码与旧密码是否相同，如果相同，那么不修改，减少数据库操作

        String oldPwd = (String) RedisUtils.hget(LOGIN_ADMIN_REFRESH_TOKEN +token,"password");

        log.info(newPwd);
        log.info(oldPwd);
        if(newPwd.equals(oldPwd)){
            return Result.error(1,"新密码与旧密码相同!");
        }
        //新旧密码不相同再修改
        adminMapper.changePwd(nowAdmin.getId(),newPwd);
        //从redis中删除token和登录信息
        RedisUtils.del(LOGIN_ADMIN_REFRESH_TOKEN +token);
        RedisUtils.del(LOGIN_ADMIN_PHONE+nowAdmin.getPhone());
        //修改密码后重新设置成不允许修改密码,所以删除redis中上次的验证码
        //再次验证验证码正确后再允许修改密码
        RedisUtils.del(ADMIN_SMS_CODE_KEY +nowAdmin.getPhone());
        return Result.ok();
    }

    @Override
    public Result refreshToken(String accessToken,String refreshToken) {
        if(ObjectUtils.isEmpty(accessToken)){
            return Result.error(HttpStatus.UNAUTHORIZED,"没有accessToken,请先登录!");
        }

        String jwtAccessToken = accessToken.substring(7);
        String userName = jwtTokenProvider.getUserName(jwtAccessToken);
        String userInRedisStr = RedisUtils.get(LOGIN_ADMIN_PHONE+userName);
        JSONObject userInRedisJson = JsonUtils.stringToJsonObj(userInRedisStr);

        String refreshTokenInRedis = (String) userInRedisJson.get("refreshToken");
        boolean refreshTokenValid = userInRedisStr!=null&&refreshTokenInRedis!=null&&refreshTokenInRedis.equals(refreshToken);

        //本地不存在RefreshToken 或者是 Redis中存储的RefreshToken已经过期,此时不允许获取新的accessToken，必须重新登录
        if(ObjectUtils.isEmpty(refreshToken)||!refreshTokenValid){
            return Result.error(HttpStatus.UNAUTHORIZED,"登陆令牌不存在或已过期,请先登录");
        }

        String newAccessToken = jwtTokenProvider.generateToken(userName,ROLE_ADMIN,ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = jwtTokenProvider.generateToken(userName,ROLE_ADMIN,REFRESH_TOKEN_EXPIRATION);

        userInRedisJson.replace("refreshToken",newRefreshToken);

        //刷新 Redis中LOGIN_USER_REFRESH_TOKEN的accessToken
        RedisUtils.hset(LOGIN_ADMIN_REFRESH_TOKEN+refreshToken,"accessToken",newAccessToken);

        //删除旧的accessToken
        RedisUtils.del(LOGIN_ADMIN_ACCESS_TOKEN+ jwtAccessToken);

        //Redis中的RefreshToken刷新
        String userJsonStr = RedisUtils.get(LOGIN_ADMIN_PHONE+userName);
        Admin user = JsonUtils.jsonStrToJavaBean(userJsonStr,Admin.class);

        user.setRefreshToken(newRefreshToken);
        RedisUtils.del(LOGIN_ADMIN_REFRESH_TOKEN+refreshToken);
        RedisUtils.storeBeanAsHash(user,LOGIN_ADMIN_REFRESH_TOKEN+newRefreshToken,REDIS_THREE_DAYS_EXPIRATION);
        //AccessToken保存20min
        RedisUtils.set(LOGIN_ADMIN_ACCESS_TOKEN+newAccessToken,"ok",20L);
        //刷新loginUser的时间
        RedisUtils.set(LOGIN_ADMIN_PHONE+userName,JsonUtils.objToJsonString(userInRedisJson),REDIS_ONE_WEEK_EXPIRATION);
        Map<String,String>tokenMap = new HashMap<>(){
            {
                put("accessToken",newAccessToken);
                put("refreshToken",newRefreshToken);
            }
        };

        return Result.ok(tokenMap);


    }


}

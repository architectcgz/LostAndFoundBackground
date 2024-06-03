package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.example.lostandfoundbackground.config.security.jwt.JwtTokenProvider;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityUserDetails;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.*;
import com.example.lostandfoundbackground.entity.User;
import com.example.lostandfoundbackground.mapper.UserMapper;
import com.example.lostandfoundbackground.service.UserService;
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
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Result register(RegisterFormDTO registerForm){
        //校验用户名、密码和手机号是否符合格式
        Map<String, Object>validateResult = ValidateUtils.validateRegisterForm(registerForm);
        Boolean isValid = (Boolean) validateResult.get("valid");
        if(!isValid){
            return Result.error(1, (String) validateResult.get("msg"));
        }
        String phone = registerForm.getPhone();
        //校验验证码
        Map<Object,Object>result = RedisUtils.hmget(USER_SMS_CODE_KEY+phone);
        String smsCode = (String) result.get("code");
        if(smsCode==null||!result.get("code").equals(registerForm.getCode())){
            return Result.error(1,"验证码不存在或已过期!");
        }
        //校验一下是否这个手机号已经注册过
        User u = userMapper.findUserBaseByPhone(phone);
        if(u!=null){
            return Result.error(1,"手机号"+phone+"已经注册过");
        }
        //上面都校验成功后,把用户信息同时存放到redis和mysql
        User user = new User();
        user.setPhone(phone);
        user.setAvatar("https://big-event-arc.oss-cn-hangzhou.aliyuncs.com/identicon.png");
        user.setName(registerForm.getName());
        user.setStatus(0);
        user.setPassword(passwordEncoder.encode(registerForm.getCode()));
        user.setCreateTime(DateTime.now());
        user.setUpdateTime(DateTime.now());
        //删除验证码，防止多次注册
        RedisUtils.del(USER_SMS_CODE_KEY+phone);
        //将注册好的用户信息先保存到数据库中
        try {
            userMapper.addUser(user);
        }catch (Exception e){
            log.info(e.getMessage());
            return Result.error(1,"数据保存错误!请联系管理员");
        }
        //如果数据库中保存成功再将信息保存到redis中
        RedisUtils.storeBeanAsHash(user,LOGIN_USER_PHONE+user.getPhone(),LOGIN_USER_TTL);
        //这里可以发放jwt,也可以不发放,登陆时一起发放
        //        var jwt = jwtTokenProvider.generatorToken(
        //                new SecurityUserDetails(
        //                        user,
        //                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"),
        //                        true, true,
        //                        true,true)
        //        );
        //        return Result.ok(jwt);
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
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        UserDTO userDTO = BeanUtil.copyProperties(userDetails.getUser(),UserDTO.class);
        return Result.ok(userDTO);
    }

    @Override
    public Result updateAvatar(String avatarUrl) {
        //TODO 修改可以直接修改缓存中的内容，之后考虑使用MQ
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        User user = userDetails.getUser();
        //更新数据库中的信息
        userMapper.updateAvatar(user.getId(),avatarUrl);
        //更新redis中的数据
        JSONObject jsonObject = JsonUtils.stringToJsonObj(RedisUtils.get(LOGIN_USER_PHONE+user.getPhone()));
        String token = (String) jsonObject.get("token");
        jsonObject.replace("avatar",avatarUrl);

        RedisUtils.set(LOGIN_USER_PHONE+user.getPhone(),jsonObject.toString());

        RedisUtils.hset(LOGIN_USER_REFRESH_TOKEN +token,"avatar",avatarUrl);
        return Result.ok();
    }

    @Override
    public Result updateUserName(String newName) {
        //TODO 修改可以直接修改缓存中的内容，之后考虑使用MQ
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        User user = userDetails.getUser();
        String username = userDetails.getUsername();
        log.info("从SecurityContextHolder中获取到的上下文对象username为"+ user.getName());
        if(newName.equals(username)){
            return Result.error(1,"新用户名与旧用户名相同!");
        }

        if(!RegexUtils.isUserNameValid(newName)){
            return Result.error(1,"用户名格式不正确!");
        }

        //更新数据库中的信息
        log.info("ID "+ user.getId()+"正在尝试修改用户名");
        userMapper.updateUserName(user.getId(),newName);
        //更新redis中的数据
        JSONObject jsonObject = JsonUtils.stringToJsonObj(RedisUtils.get(LOGIN_USER_PHONE+user.getPhone()));
        String token = (String) jsonObject.get("token");
        jsonObject.replace("avatar",newName);

        RedisUtils.set(LOGIN_USER_PHONE+user.getPhone(),jsonObject.toString());

        RedisUtils.hset(LOGIN_USER_REFRESH_TOKEN +token,"name",newName);
        return Result.ok();
    }
    /*
        刷新token
        刷新时会把参数中的refreshToken与redis中的refreshToken进行对比
        如果相同才允许刷新。
        刷新后,用户会拿到新的refreshToken与accessToken
        用户必须拥有新的refreshToken后才能再次刷新token
        否则禁止刷新token,防止一个refreshToken无限刷新
     */
    @Override
    public Result refreshToken(String accessToken,String refreshToken) {
        if(ObjectUtils.isEmpty(accessToken)){
            return Result.error(HttpStatus.UNAUTHORIZED,"没有accessToken,请先登录!");
        }

        String jwtAccessToken = accessToken.substring(7);
        String userName = jwtTokenProvider.getUserName(jwtAccessToken);

        log.info("ThreadLocal中的refreshToken: "+refreshToken);
        String userInRedisStr = RedisUtils.get(LOGIN_USER_PHONE+userName);
        JSONObject userInRedisJson = JsonUtils.stringToJsonObj(userInRedisStr);
        String refreshTokenInRedis = (String) userInRedisJson.get("refreshToken");
        boolean refreshTokenValid = userInRedisStr!=null&&refreshTokenInRedis!=null&&refreshTokenInRedis.equals(refreshToken);

        //本地不存在RefreshToken 或者是 Redis中存储的RefreshToken已经过期,此时不允许获取新的accessToken，必须重新登录
        if(ObjectUtils.isEmpty(refreshToken)||!refreshTokenValid){
            return Result.error(HttpStatus.UNAUTHORIZED,"登陆令牌不存在或已过期,请先登录");
        }

        String newAccessToken = jwtTokenProvider.generateToken(userName,ROLE_USER,ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = jwtTokenProvider.generateToken(userName,ROLE_USER,REFRESH_TOKEN_EXPIRATION);

        userInRedisJson.replace("refreshToken",newRefreshToken);

        //刷新 Redis中LOGIN_USER_REFRESH_TOKEN的accessToken
        RedisUtils.hset(LOGIN_USER_REFRESH_TOKEN+refreshToken,"accessToken",newAccessToken);

        //删除旧的accessToken
        log.info("旧的accessToken: "+jwtAccessToken);
        RedisUtils.del(LOGIN_USER_ACCESS_TOKEN+ jwtAccessToken);

        //Redis中的RefreshToken刷新
        String userJsonStr = RedisUtils.get(LOGIN_USER_PHONE+userName);
        User user = JsonUtils.jsonStrToJavaBean(userJsonStr,User.class);

        user.setRefreshToken(newRefreshToken);
        //删除旧的refreshToken
        RedisUtils.del(LOGIN_USER_REFRESH_TOKEN+refreshToken);
        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);

        RedisUtils.storeBeanAsHash(userDTO,LOGIN_USER_REFRESH_TOKEN+newRefreshToken,REDIS_THREE_DAYS_EXPIRATION);
        //AccessToken保存20min
        RedisUtils.set(LOGIN_USER_ACCESS_TOKEN+newAccessToken,"ok",20L);
        //刷新loginUser的时间
        RedisUtils.set(LOGIN_USER_PHONE+userName,JsonUtils.objToJsonString(userInRedisJson),REDIS_ONE_WEEK_EXPIRATION);
        Map<String,String>tokenMap = new HashMap<>(){
            {
                put("accessToken",newAccessToken);
                put("refreshToken",newRefreshToken);
            }
        };

        return Result.ok(tokenMap);
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
        //校验手机号和密码
        String phone = loginForm.getPhone();
        String password = loginForm.getPassword();
        if(!ValidateUtils.validateLoginForm(loginForm)){
            return Result.error(1,"手机号码或密码格式错误");
        }
        log.info("开始验证User: "+phone+" 的密码是否正确");
        //验证密码是否正确
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        phone,password
                )
        );
        log.info("密码是否正确验证完成");

        //从redis中尝试查询user，不存在再去MySql查询
        User user = null;
        String key = LOGIN_USER_PHONE + phone;
        try {
            if(RedisUtils.hasKey(key)){
                String jsonUser = RedisUtils.get(key);
                //将json反序列化为administrator类型
                user = JsonUtils.jsonStrToJavaBean(jsonUser, User.class);
                //在其他地点以及登录，删除上次登录的token，挤掉上次的登录
                String oldRefreshToken = user.getRefreshToken();
                String oldAccessToken = (String) RedisUtils.hget(LOGIN_USER_REFRESH_TOKEN+oldRefreshToken,"accessToken");
                log.info("OldRefreshToken: "+oldRefreshToken);
                log.info("OldAccessToken: "+oldAccessToken);
                RedisUtils.del(LOGIN_USER_ACCESS_TOKEN +oldAccessToken);
                RedisUtils.del(LOGIN_USER_REFRESH_TOKEN+oldRefreshToken);
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
        String refreshToken = jwtTokenProvider.generateToken(user.getPhone(),ROLE_USER, REFRESH_TOKEN_EXPIRATION); //设置成72小时过期
        String accessToken = jwtTokenProvider.generateToken(user.getPhone(),ROLE_USER,ACCESS_TOKEN_EXPIRATION);//accessToken 20分钟过期
        user.setRefreshToken(refreshToken);

        Map<String,String> tokenMap = jwtTokenProvider.generateTokenMap(accessToken,refreshToken);
        //将User转化为HashMap
        //这里只需要用到一些关键数据：id、phone、name,level,不需要使用createTime和updateTime
        //所以使用userDTO接收
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        userDTO.setAccessToken(accessToken);
        //设置refreshToken有效期 三天失效
        RedisUtils.storeBeanAsHash(userDTO, LOGIN_USER_REFRESH_TOKEN + refreshToken,REDIS_THREE_DAYS_EXPIRATION);
        //设置accessToken 20分钟过期
        RedisUtils.set(LOGIN_USER_ACCESS_TOKEN+accessToken,"ok",20L);

        //mysql中查到的User信息放到redis里，下次登录可以先从redis中查询
        //设置7天失效
        RedisUtils.storeBeanAsJson(user,LOGIN_USER_PHONE + phone,REDIS_ONE_WEEK_EXPIRATION);

        //返回token
        return Result.ok(tokenMap);
    }

    @Override
    public Result logout(String token) {
        if(!StringUtils.hasLength(token)){
            return Result.error(HttpStatus.UNAUTHORIZED,"用户未登录!");
        }
        RedisUtils.del(LOGIN_USER_REFRESH_TOKEN +token);
        return Result.ok();
    }

    @Override
    public Result sendSmsCode() {
        //从ThreadLocal中获取到当前的用户
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        User nowUser = userDetails.getUser();
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
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        String phone = userDetails.getUsername();
        return ValidateUtils.validateSmsCode(USER_SMS_CODE_KEY,phone,code);
    }

    @Override
    public Result modifyPwd(String token, ChangePwdDTO changePwdDTO) {
        //从ThreadLocal中获取到当前的管理员
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextUtils.getLocalUserDetail();
        User nowUser = userDetails.getUser();
        if(nowUser==null){
            return Result.error(1,"请检查账号登录状态");
        }
        if(!changePwdDTO.getNewPwd().equals(changePwdDTO.getRepeatPwd())){
            return Result.error(1,"两次输入的密码不相同,请检查");
        }
        //先尝试修改密码，然后将当前Redis中存放的管理员信息删除(包括token和登录信息)
        String newPwd = passwordEncoder.encode(changePwdDTO.getRepeatPwd());
        //对比新密码与旧密码是否相同，如果相同，那么不修改，减少数据库操作
        String oldPwd = (String) RedisUtils.hget(LOGIN_USER_REFRESH_TOKEN +token,"password");
        if(newPwd.equals(oldPwd)){
            return Result.error(1,"新密码与旧密码相同!");
        }
        //新旧密码不相同再修改
        userMapper.changePwd(nowUser.getId(),newPwd);
        //从redis中删除token和登录信息
        RedisUtils.del(LOGIN_USER_REFRESH_TOKEN +token);
        RedisUtils.del(LOGIN_USER_PHONE+nowUser.getPhone());
        //修改密码后重新设置成不允许修改密码,所以删除redis中上次的验证码
        //再次验证验证码正确后再允许修改密码
        RedisUtils.del(USER_SMS_CODE_KEY +nowUser.getPhone());
        return Result.ok();
    }

}

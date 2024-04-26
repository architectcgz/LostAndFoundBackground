package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.example.lostandfoundbackground.dto.AdministratorDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Administrator;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.service.AdminService;
import com.example.lostandfoundbackground.utils.EncryptUtil;
import com.example.lostandfoundbackground.utils.RegexUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_ADMIN_KEY;
import static com.example.lostandfoundbackground.constants.RedisConstants.LOGIN_ADMIN_TTL;

/**
 * @author archi
 */
@Service
public class AdminServiceImpl implements AdminService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AdminMapper adminMapper;
    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        //校验手机号和密码
        String phone = loginForm.getPhone();
        String password = loginForm.getPassword();
        if(!(RegexUtils.isPhoneValid(phone)&&RegexUtils.isPasswordValid(password))){
            //不符合手机号的格式，返回错误信息
            return Result.fail("手机号码或密码格式错误");
        }
        //得到加密后的字符串
        String encryptedPwd = EncryptUtil.getMD5String(password);
        //从redis中尝试查询用户，不存在再去MySql查询
        Administrator admin = null;
        try {
            if(stringRedisTemplate.hasKey("admin"+phone)){
                String jsonAdmin = stringRedisTemplate.opsForValue().get("admin"+phone);
                //将json反序列化为administrator类型
                admin = MAPPER.readValue(jsonAdmin,Administrator.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //redis中查询不到,从mysql中查询
        if(admin == null){
            admin = adminMapper.findAdminByPhone(phone);
            if(admin == null){
                return Result.fail("该管理员不存在");
            }
        }
        //前端传来的加密后的密码与实际密码不同，密码错误
        if(!admin.getPassword().equals(encryptedPwd)){
            return Result.fail("密码错误,请重新输入");
        }
        //到这里，管理员存在且密码正确，发放令牌
        //UUID.randomUUID().toString(true) true参数表示UUID中不含有'-'
        String token = UUID.randomUUID().toString(true);

        //将Administrator转化为HashMap
        AdministratorDTO adminDTO = BeanUtil.copyProperties(admin,AdministratorDTO.class);
        Map<String,Object> adminMap = BeanUtil.beanToMap(adminDTO,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName,fieldValue)->fieldValue.toString()));

        //将Administrator的token和administrator的信息转化为HashMap存放到redis中
        String tokenKey = LOGIN_ADMIN_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey,adminMap);
        //设置token有效期
        stringRedisTemplate.expire(tokenKey,LOGIN_ADMIN_TTL, TimeUnit.MINUTES);
        //返回token
        return Result.ok(token);
    }
}

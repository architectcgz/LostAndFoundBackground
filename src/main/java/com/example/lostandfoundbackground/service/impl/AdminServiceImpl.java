package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.example.lostandfoundbackground.dto.AdministratorDTO;
import com.example.lostandfoundbackground.dto.LoginFormDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Administrator;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.service.AdminService;
import com.example.lostandfoundbackground.utils.EncryptUtil;
import com.example.lostandfoundbackground.utils.JsonUtils;
import com.example.lostandfoundbackground.utils.RedisUtil;
import com.example.lostandfoundbackground.utils.RegexUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;

/**
 * @author archi
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AdminMapper adminMapper;
    @Override
    public Result login(LoginFormDTO loginForm) {
        //校验手机号和密码
        String phone = loginForm.getPhone();
        String password = loginForm.getPassword();
        if(!(RegexUtils.isPhoneValid(phone)&&RegexUtils.isPasswordValid(password))){
            //不符合手机号的格式，返回错误信息
            log.info("手机号是否符合格式:"+ RegexUtils.isPhoneValid(phone));
            log.info("密码是否符合格式"+ RegexUtils.isPasswordValid(password));
            return Result.fail("手机号码或密码格式错误");
        }
        //得到加密后的字符串
        String encryptedPwd = EncryptUtil.getMD5String(password);
        //从redis中尝试查询Admin，不存在再去MySql查询
        Administrator admin = null;
        String key = LOGIN_ADMIN_PHONE + phone;
        try {
            if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
                String jsonAdmin = stringRedisTemplate.opsForValue().get(key);
                //将json反序列化为administrator类型
                admin = JsonUtils.jsonToPojo(jsonAdmin, Administrator.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //redis中查询不到,从mysql中查询
        if(admin == null){
            admin = adminMapper.findAdminBaseByPhone(phone);
            if(admin == null){
                return Result.fail("该管理员不存在");
            }
            //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
            //设置3天失效3*24*60
            RedisUtil.storeBeanAsJson(stringRedisTemplate,admin,LOGIN_ADMIN_PHONE + phone,4320);
        }



        //前端传来的加密后的密码与实际密码不同，密码错误
        if(!admin.getPassword().equals(encryptedPwd)){
            return Result.fail("密码错误,请重新输入");
        }
        //到这里，管理员存在且密码正确，发放令牌
        //UUID.randomUUID().toString(true) true参数表示UUID中不含有'-'
        String token = UUID.randomUUID().toString(true);

        //将Administrator转化为HashMap
        //这里只需要用到一些关键数据：id、phone、name,level,不需要使用createTime和updateTime
        //所以使用adminDTO接收
        AdministratorDTO adminDTO = BeanUtil.copyProperties(admin,AdministratorDTO.class);
        //设置token有效期 一天失效
        RedisUtil.storeBeanAsHash(stringRedisTemplate,adminDTO,LOGIN_ADMIN_KEY + token,1440);
        //返回token
        return Result.ok(token);
    }

    /*
        登出需要清除Redis中保存的token
     */
    @Override
    public Result logout(String token) {
        if(!StringUtils.hasLength(token)){
            return Result.fail("用户未登录!");
        }
        stringRedisTemplate.delete(LOGIN_ADMIN_KEY+token);
        return Result.ok();
    }
}

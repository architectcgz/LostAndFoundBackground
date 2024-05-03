package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.*;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.service.AdminService;
import com.example.lostandfoundbackground.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.example.lostandfoundbackground.constants.RedisConstants.*;

/**
 * @author archi
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
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
            return Result.error(1,"手机号码或密码格式错误");
        }
        //得到加密后的字符串
        String encryptedPwd = EncryptUtil.getMD5String(password);
        //从redis中尝试查询Admin，不存在再去MySql查询
        Admin admin = null;
        String key = LOGIN_ADMIN_PHONE + phone;
        try {
            if(RedisUtils.hasKey(key)){
                String jsonAdmin = RedisUtils.get(key);
                //将json反序列化为administrator类型
                admin = JsonUtils.jsonToJavaBean(jsonAdmin,Admin.class);
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
            //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
            //设置3天失效3*24*60
            RedisUtils.storeBeanAsJson(admin,LOGIN_ADMIN_PHONE + phone,4320);
        }

        //前端传来的加密后的密码与实际密码不同，密码错误
        if(!admin.getPassword().equals(encryptedPwd)){
            return Result.error(1,"密码错误,请重新输入");
        }
        //到这里，管理员存在且密码正确，发放令牌
        //UUID.randomUUID().toString(true) true参数表示UUID中不含有'-'
        String token = UUID.randomUUID().toString(true);

        //将Administrator转化为HashMap
        //这里只需要用到一些关键数据：id、phone、name,level,不需要使用createTime和updateTime
        //所以使用adminDTO接收
        AdminDTO adminDTO = BeanUtil.copyProperties(admin, AdminDTO.class);
        //设置token有效期 一天失效
        RedisUtils.storeBeanAsHash(adminDTO,LOGIN_ADMIN_KEY + token,1440);
        //返回token
        return Result.ok(token);
    }

    /*
        登出需要清除Redis中保存的token
     */
    @Override
    public Result logout(String token) {
        if(!StringUtils.hasLength(token)){
            return Result.error(HttpStatus.UNAUTHORIZED,"用户未登录!");
        }
        RedisUtils.del(LOGIN_ADMIN_KEY+token);
        return Result.ok();
    }





    @Override
    public Result addAdmin(Admin admin) {
        //从ThreadLocal中获取到当前的管理员
        Thread t = Thread.currentThread();
        log.info("取出ThreadLocal中的数据时当前线程:"+t.getName());
        AdminDTO nowAdmin = ThreadLocalUtil.get();
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
        AdminDTO nowAdmin = ThreadLocalUtil.get();
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
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        String phone = nowAdmin.getPhone();
        //发送验证码
        String smsCode = RandomUtil.randomNumbers(6);
        try {
            AliYunSmsUtil.sendSms(phone,smsCode);
            log.info("验证码:"+smsCode+"\t发送到手机号:"+phone);
        }catch (Exception e){
            log.info(e.getMessage());
            return Result.error(HttpStatus.INTERNAL_ERROR,"发送验证码失败"+e.getMessage());
        }
        //把验证码存放到redis里，失效时间设置为5min
        RedisUtils.set(SMS_CODE_KEY+phone,smsCode,5);
        return Result.ok();
    }

    @Override
    public Result validateSmsCode(String code) {
        //从ThreadLocal中获取到当前的管理员
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        String phone = nowAdmin.getPhone();
        String key = SMS_CODE_KEY +phone;
        if(!RedisUtils.hasKey(key)){
            return Result.error(1,"该验证码不存在!");
        }
        String codeInRedis = RedisUtils.get(key);
        log.info("在Redis中保存的验证码是:"+codeInRedis);
        if (codeInRedis == null || !codeInRedis.equals(code)) {
            return Result.error(1,"输入的验证码错误或已过期");
        }
        return Result.ok("验证码验证成功");
    }

    @Override
    public Result changePwd(String token,ChangePwdDTO changePwdDTO) {

        if(!changePwdDTO.getNewPwd().equals(changePwdDTO.getRepeatPwd())){
            return Result.error(1,"两次输入的密码不相同,请检查");
        }
        //从ThreadLocal中获取到当前的管理员
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        //先修改密码，然后将当前登录的管理员登出
        String newPwd = EncryptUtil.getMD5String(changePwdDTO.getRepeatPwd());
        adminMapper.changePwd(nowAdmin.getId(),newPwd);
        //从redis中删除token
        RedisUtils.del(LOGIN_ADMIN_KEY+token);
        return Result.ok();
    }

    @Override
    public Result addNews(NewsDTO newsDTO) {

        return Result.ok();
    }




}

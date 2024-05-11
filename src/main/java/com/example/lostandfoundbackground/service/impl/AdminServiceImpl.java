package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.*;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.entity.Notification;
import com.example.lostandfoundbackground.mapper.AdminMapper;
import com.example.lostandfoundbackground.mapper.NotificationMapper;
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
    @Resource
    private NotificationMapper notificationMapper;

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
        //从redis中尝试查询Admin，不存在再去MySql查询
        Admin admin = null;
        String key = LOGIN_ADMIN_PHONE + phone;
        try {
            if(RedisUtils.hasKey(key)){
                String jsonAdmin = RedisUtils.get(key);
                //将json反序列化为administrator类型
                admin = JsonUtils.jsonToJavaBean(jsonAdmin,Admin.class);
                //在其他地点以及登录，删除上次登录的token，挤掉上次的登录
                String oldToken = admin.getToken();
                if(oldToken!=null){
                    RedisUtils.del(LOGIN_ADMIN_KEY+oldToken);
                }
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

        admin.setToken(token);
        //mysql中查到的信息放到redis里，下次登录可以先从redis中查询
        //设置3天失效3*24*60
        RedisUtils.storeBeanAsJson(admin,LOGIN_ADMIN_PHONE + phone,4320);

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
        if(!RegexUtils.isPasswordValid(phone)){
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
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        String phone = nowAdmin.getPhone();
        return ValidateUtils.validateSmsCode(ADMIN_SMS_CODE_KEY,phone,code);
    }

    @Override
    public Result modifyPwd(String token, ChangePwdDTO changePwdDTO) {
        //从ThreadLocal中获取到当前的管理员
        log.info("修改密码的线程:"+Thread.currentThread().getName());
        AdminDTO nowAdmin = ThreadLocalUtil.get();

        if(!changePwdDTO.getNewPwd().equals(changePwdDTO.getRepeatPwd())){
            return Result.error(1,"两次输入的密码不相同,请检查");
        }

        //先尝试修改密码，然后将当前Redis中存放的管理员信息删除(包括token和登录信息)
        String newPwd = EncryptUtil.getMD5String(changePwdDTO.getRepeatPwd());
        //对比新密码与旧密码是否相同，如果相同，那么不修改，减少数据库操作

        String oldPwd = (String) RedisUtils.hget(LOGIN_ADMIN_KEY+token,"password");

        log.info(newPwd);
        log.info(oldPwd);
        if(newPwd.equals(oldPwd)){
            return Result.error(1,"新密码与旧密码相同!");
        }
        //新旧密码不相同再修改
        adminMapper.changePwd(nowAdmin.getId(),newPwd);
        //从redis中删除token和登录信息
        RedisUtils.del(LOGIN_ADMIN_KEY+token);
        RedisUtils.del(LOGIN_ADMIN_PHONE+nowAdmin.getPhone());
        //修改密码后重新设置成不允许修改密码,所以删除redis中上次的验证码
        //再次验证验证码正确后再允许修改密码
        RedisUtils.del(ADMIN_SMS_CODE_KEY +nowAdmin.getPhone());
        return Result.ok();
    }

    @Override
    public Result addNotification(NotificationDTO notificationDTO) {
        Notification notification = BeanUtil.copyProperties(notificationDTO,Notification.class);
        AdminDTO nowAdmin = ThreadLocalUtil.get();
        notification.setCreateAdmin(nowAdmin.getId());
        notification.setCreateTime(DateTime.now());
        notification.setUpdateTime(DateTime.now());
        notificationMapper.add(notification);
        return Result.ok();
    }
    @Override
    public Result deleteNotification(Long id) {
        notificationMapper.delete(id);
        return Result.ok();
    }



}

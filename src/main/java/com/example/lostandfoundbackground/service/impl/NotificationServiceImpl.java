package com.example.lostandfoundbackground.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.example.lostandfoundbackground.config.security.userDetails.SecurityAdminDetails;
import com.example.lostandfoundbackground.dto.AdminDTO;
import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Admin;
import com.example.lostandfoundbackground.entity.Notification;
import com.example.lostandfoundbackground.mapper.NotificationMapper;
import com.example.lostandfoundbackground.service.NotificationService;
import com.example.lostandfoundbackground.utils.SecurityContextUtils;
import com.example.lostandfoundbackground.utils.ThreadLocalUtil;
import com.example.lostandfoundbackground.utils.ValidateUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author archi
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Resource
    private NotificationMapper notificationMapper;
    @Override
    public Result getNotificationList(Long offset, Long pageSize) {
        if(offset<0){
            return Result.error(1,"请填写正确的参数");
        }
        List<Notification>notificationList = notificationMapper.getList(offset,pageSize);
        return Result.ok(notificationList,(long)notificationList.size());
    }

    @Override
    public Result searchNotificationByTitle(String title) {
        if(ObjectUtils.isEmpty(title)){
            return Result.error(1,"请填写正确的参数!");
        }
        List<Notification>notificationList = notificationMapper.searchByTitle(title);
        return Result.ok(notificationList,(long)notificationList.size());
    }

    @Override
    public Result searchNotificationByPublisher(String publisher) {
        if(ObjectUtils.isEmpty(publisher)){
            return Result.error(1,"请填写正确的参数!");
        }
        List<Notification> notificationList = notificationMapper.searchByAdminName(publisher);
        return Result.ok(notificationList,(long)notificationList.size());
    }

    @Override
    public Result deleteNotification(Long id) {
        notificationMapper.delete(id);
        return Result.ok();
    }

    @Override
    public Result addNotification(NotificationDTO notificationDTO) {
        Map<String,Object>resultMap = ValidateUtils.validateNotificationForm(notificationDTO);
        boolean isValid = (boolean) resultMap.get("valid");
        if(!isValid){
            return Result.error(1, (String) resultMap.get("msg"));
        }
        Notification notification = BeanUtil.copyProperties(notificationDTO,Notification.class);
        SecurityAdminDetails securityAdminDetails = (SecurityAdminDetails) SecurityContextUtils.getLocalUserDetail();
        Admin nowAdmin = securityAdminDetails.getAdmin();
        notification.setCreateAdmin(nowAdmin.getId());
        notification.setCreateTime(DateTime.now());
        notification.setUpdateTime(DateTime.now());
        notificationMapper.add(notification);
        return Result.ok();
    }
}

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
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author archi
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Resource
    private NotificationMapper notificationMapper;
    @Override
    public List<Notification> getNotificationList(Long offset, Long pageSize) {
        return notificationMapper.getList(offset,pageSize);
    }

    @Override
    public List<Notification> searchNotificationByTitle(String title) {
        return notificationMapper.searchByTitle(title);
    }

    @Override
    public List<Notification> searchNotificationByPublisher(String publisher) {
        return notificationMapper.searchByAdminName(publisher);
    }

    @Override
    public Result deleteNotification(Long id) {
        notificationMapper.delete(id);
        return Result.ok();
    }

    @Override
    public Result addNotification(NotificationDTO notificationDTO) {
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

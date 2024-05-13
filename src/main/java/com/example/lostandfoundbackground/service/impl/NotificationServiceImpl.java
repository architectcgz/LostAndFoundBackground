package com.example.lostandfoundbackground.service.impl;

import com.example.lostandfoundbackground.entity.Notification;
import com.example.lostandfoundbackground.mapper.NotificationMapper;
import com.example.lostandfoundbackground.service.NotificationService;
import jakarta.annotation.Resource;
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
}

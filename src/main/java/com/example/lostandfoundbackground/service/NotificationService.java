package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Notification;

import java.util.List;

/**
 * @author archi
 */
public interface NotificationService {
    Result getNotificationList(Long offset, Long pageSize);

    Result searchNotificationByTitle(String title);

    Result searchNotificationByPublisher(String publisher);

    Result deleteNotification(Long id);

    Result addNotification(NotificationDTO notificationDTO);
}

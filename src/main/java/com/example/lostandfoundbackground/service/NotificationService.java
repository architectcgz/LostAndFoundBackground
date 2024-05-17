package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Notification;

import java.util.List;

/**
 * @author archi
 */
public interface NotificationService {
    List<Notification> getNotificationList(Long offset, Long pageSize);

    List<Notification> searchNotificationByTitle(String title);

    List<Notification> searchNotificationByPublisher(String publisher);

    Result deleteNotification(Long id);

    Result addNotification(NotificationDTO notificationDTO);
}

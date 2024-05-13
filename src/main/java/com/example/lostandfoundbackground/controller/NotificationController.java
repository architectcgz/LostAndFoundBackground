package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.entity.Notification;
import com.example.lostandfoundbackground.service.NotificationService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author archi
 */
@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Resource
    private NotificationService notificationService;
    @GetMapping
    public Result getNotifications(@RequestParam Long pageNum,@RequestParam Long pageSize){
        List<Notification> result = notificationService.getNotificationList((pageNum-1L)*pageSize,pageSize);

        return Result.ok(result,(long)result.size());
    }
    @GetMapping("/search/title")
    public Result searchNotificationByTitle(@RequestParam("title")@NotEmpty String title){
        List<Notification> result = notificationService.searchNotificationByTitle(title);
        return Result.ok(result,(long)result.size());
    }

    @GetMapping("/search/publisher")
    public Result searchNotificationByPublisher(@RequestParam("publisher")@NotEmpty String publisher){
        List<Notification> result = notificationService.searchNotificationByPublisher(publisher);
        return Result.ok(result,(long)result.size());
    }

}

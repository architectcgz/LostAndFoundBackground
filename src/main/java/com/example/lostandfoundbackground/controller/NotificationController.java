package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.NotificationService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author archi
 */
@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Resource
    private NotificationService notificationService;
    @GetMapping("/list")
    public Result getNotifications(@RequestParam Long pageNum,@RequestParam Long pageSize){
        return notificationService.getNotificationList((pageNum-1L)*pageSize,pageSize);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/publish")
    public Result publishNotification(@RequestBody NotificationDTO notificationDTO){
        return notificationService.addNotification(notificationDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public Result deleteNotification(@PathVariable("id")Long id){
        return notificationService.deleteNotification(id);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/title")
    public Result searchNotificationByTitle(@RequestParam("title")@NotEmpty String title){
        return notificationService.searchNotificationByTitle(title);
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/search/publisher")
    public Result searchNotificationByPublisher(@RequestParam("publisher")@NotEmpty String publisher){
        return notificationService.searchNotificationByPublisher(publisher);
    }

}

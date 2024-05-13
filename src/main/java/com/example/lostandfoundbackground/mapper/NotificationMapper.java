package com.example.lostandfoundbackground.mapper;

import com.example.lostandfoundbackground.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author archi
 */
@Mapper
public interface NotificationMapper {
    void add(Notification n);

    void delete(Long id);

    List<Notification> getList(Long offset,Long pageSize);

    Notification getById(Long id);

    List<Notification> searchByTitle(String title);

    List<Notification> searchByAdminName(String adminName);
}

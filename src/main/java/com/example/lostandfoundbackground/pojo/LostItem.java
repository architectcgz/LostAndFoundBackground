package com.example.lostandfoundbackground.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
public class LostItem {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String name;
    private Boolean founded;//是否已经找到
    private String image;
    private Long categoryId;
    private String foundLocation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime foundTime;
    private String description;
    private String ownerName;//失主名称
    private String phone;
    private Long createUser;//创建的用户id
}

package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
public class FoundItem {
    @NotNull(groups = Update.class,message = "id不能为空")
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String name;
    private Boolean claimed;
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

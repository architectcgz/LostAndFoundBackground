package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoundItemDTO {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String name;
    private Integer claimed;
    private String image;
    private Long categoryId;
    private String foundLocation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime foundTime;
    private String description;
    private String ownerName;//失主名称
    private String phone;
    private Long createUser;//创建的用户id
}

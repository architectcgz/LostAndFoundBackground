package com.example.lostandfoundbackground.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
public class Category {
    @NotEmpty(groups = Update.class,message = "id不能为空")
    private Long id;
    private String categoryName;
    private String categoryAlias;
    private Long createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

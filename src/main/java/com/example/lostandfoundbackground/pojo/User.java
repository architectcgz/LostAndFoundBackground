package com.example.lostandfoundbackground.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
public class User {
    @NotNull(groups = Update.class,message = "id不能为空")
    private Long id;
    @JsonIgnore//Json转换时忽略password这个属性
    private String password;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String name;

    @NotEmpty
    @URL
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

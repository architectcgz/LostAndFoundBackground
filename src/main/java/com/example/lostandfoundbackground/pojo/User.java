package com.example.lostandfoundbackground.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
public class User {
    @NotNull
    private Long id;
    private String username;
    @JsonIgnore//Json转换时忽略password这个属性
    private String password;

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String name;

    @NotEmpty
    private String avatar;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}

package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
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
public class AdministratorDTO {
    @NotEmpty(groups = Update.class,message = "id不能为空")
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String name;
    @JsonIgnore
    private String password;
    private String phone;

}

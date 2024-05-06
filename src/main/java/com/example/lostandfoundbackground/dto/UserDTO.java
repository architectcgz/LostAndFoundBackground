package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
    private Long id;
    private String phone;
    private String name;

    private String avatar;

    @Override
    public String toString(){
        return "用户"+id+"\n姓名:"+name+"\n电话:"+phone;
    }

//    @NotNull(groups = Update.class,message = "id不能为空")
//    private Long id;
//    @JsonIgnore//Json转换时忽略password这个属性
//    private String password;
//
//    @NotEmpty
//    @Pattern(regexp = "^\\S{1,10}$")
//    private String name;
//
//    @NotEmpty
//    @URL
//    private String avatar;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime updateTime;
}

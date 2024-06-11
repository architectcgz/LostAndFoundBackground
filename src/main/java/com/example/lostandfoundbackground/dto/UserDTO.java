package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String password;
    private String avatar;
    private String accessToken;
    @Override
    public String toString(){
        return "用户"+id+"\n姓名:"+name+"\n电话:"+phone+"\n密码:"+password;
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

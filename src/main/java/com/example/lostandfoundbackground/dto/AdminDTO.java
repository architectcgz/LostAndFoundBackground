package com.example.lostandfoundbackground.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.intellij.lang.annotations.RegExp;

import static com.example.lostandfoundbackground.constants.RegexPatterns.PHONE_REGEX;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO{
    private Long id;
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = PHONE_REGEX,message = "手机号格式错误")
    private String phone;
    private String name;
    private String password;
    private Integer level;
    private String accessToken;
    @Override
    public String toString(){
        return "管理员"+id+"\n姓名:"+name+"\n电话:"+phone+"\n密码:"+password+"\n等级:"+level;
    }
//    @NotEmpty(groups = Update.class,message = "id不能为空")
//    private Long id;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime updateTime;
//    private String name;
//    @JsonIgnore
//    private String password;
//    private String phone;
}

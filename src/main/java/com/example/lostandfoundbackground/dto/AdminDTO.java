package com.example.lostandfoundbackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO{
    private Long id;
    private String phone;
    private String name;

    //用于判断admin或者user是否具有修改密码的权限
    private Boolean allowModifyPwd = false;
    private Integer level;
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

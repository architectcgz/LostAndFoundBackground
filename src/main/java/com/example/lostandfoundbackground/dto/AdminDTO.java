package com.example.lostandfoundbackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    private Integer level;
    @Override
    public String toString(){
        return "管理员"+id+"\n姓名:"+name+"\n电话:"+phone+"\n等级:"+level;
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

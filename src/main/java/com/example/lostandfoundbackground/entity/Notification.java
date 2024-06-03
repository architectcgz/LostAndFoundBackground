package com.example.lostandfoundbackground.entity;

import com.example.lostandfoundbackground.dto.NotificationDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
* 
* @author archi
 * @TableName notification
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {

    /**
    * 管理员通知的id
    */
    @NotNull(message="[管理员通知的id]不能为空")
    @ApiModelProperty("管理员通知的id")
    private Long id;
    /**
    * 通知的标题
    */
    @NotBlank(message="[通知的标题]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("通知的标题")
    @Length(max= 20,message="编码长度不能超过20")
    private String title;
    /**
    * 文章的描述
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章的描述")
    @Length(max= 50,message="编码长度不能超过50")
    private String description;
    /**
    * 创建人的id
    */
    @ApiModelProperty("创建人的id")
    private Long createAdmin;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    /**
    * 通知具体内容,字数在2000以内
    */
    @Size(max= 2000,message="编码长度不能超过2000")
    @ApiModelProperty("通知具体内容,字数在2000以内")
    @Length(max= 2000,message="编码长度不能超过2,000")
    private String content;


}

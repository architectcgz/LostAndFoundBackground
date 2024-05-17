package com.example.lostandfoundbackground.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
* 
* @author archi
 * @TableName user
*/
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long id;

    private String refreshToken;
    /**
    * 创建时间
    */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
    * 更新时间
    */
    @NotNull(message="[更新时间]不能为空")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;
    /**
    * 用户名
    */
    @Size(max= 10,message="编码长度不能超过10")
    @ApiModelProperty("用户名")
    @Length(max= 10,message="编码长度不能超过10")
    private String name;
    /**
    * 密码
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("密码")
    @Length(max= 50,message="编码长度不能超过50")
    private String password;
    /**
    * 用户头像
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("用户头像")
    @Length(max= 50,message="编码长度不能超过50")
    private String avatar;
    /**
    * 手机号码,一个手机号只能注册一个账号
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("手机号码,一个手机号只能注册一个账号")
    @Length(max= 20,message="编码长度不能超过20")
    private String phone;

    /*
    用户的状态，0表示正常使用，-1表示被禁用
     */
    @ApiModelProperty("用户的状态")
    private Integer status;
}

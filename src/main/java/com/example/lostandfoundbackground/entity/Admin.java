package com.example.lostandfoundbackground.entity;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
* 
* @author archi
 * @TableName administrator
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Getter
@Setter
public class Admin implements Serializable {

    /**
    * 
    */
    @ApiModelProperty("管理员编号")
    private Integer id;
    /**
    * 创建时间
    */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
    * 更新时间
    */
    @NotNull(message="[更新时间]不能为空")
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
    * 管理员名称
    */
    @Size(max= 25,message="编码长度不能超过25")
    @ApiModelProperty("管理员名称")
    @Length(max= 25,message="编码长度不能超过25")
    private String name;
    /**
    * 密码
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("密码")
    @Length(max= 50,message="编码长度不能超过50")
    private String password;
    /**
    * 手机号码,一个手机号只能注册一个账号
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("手机号码,一个手机号只能注册一个账号")
    @Length(max= 20,message="编码长度不能超过20")
    private String phone;
    /**
    * 管理员的等级
    */
    @ApiModelProperty("管理员的等级")
    private Integer level;

}
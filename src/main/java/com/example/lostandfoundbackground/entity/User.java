package com.example.lostandfoundbackground.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
* 
* @TableName user
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
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

    /**
    * 
    */
    private void setId(Integer id){
    this.id = id;
    }

    /**
    * 创建时间
    */
    private void setCreateTime(Date createTime){
    this.createTime = createTime;
    }

    /**
    * 更新时间
    */
    private void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }

    /**
    * 用户名
    */
    private void setName(String name){
    this.name = name;
    }

    /**
    * 密码
    */
    private void setPassword(String password){
    this.password = password;
    }

    /**
    * 用户头像
    */
    private void setAvatar(String avatar){
    this.avatar = avatar;
    }

    /**
    * 手机号码,一个手机号只能注册一个账号
    */
    private void setPhone(String phone){
    this.phone = phone;
    }


    /**
    * 
    */
    private Integer getId(){
    return this.id;
    }

    /**
    * 创建时间
    */
    private Date getCreateTime(){
    return this.createTime;
    }

    /**
    * 更新时间
    */
    private Date getUpdateTime(){
    return this.updateTime;
    }

    /**
    * 用户名
    */
    private String getName(){
    return this.name;
    }

    /**
    * 密码
    */
    private String getPassword(){
    return this.password;
    }

    /**
    * 用户头像
    */
    private String getAvatar(){
    return this.avatar;
    }

    /**
    * 手机号码,一个手机号只能注册一个账号
    */
    private String getPhone(){
    return this.phone;
    }

}

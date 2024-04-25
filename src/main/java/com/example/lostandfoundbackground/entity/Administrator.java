package com.example.lostandfoundbackground.entity;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
* 
* @TableName administrator
*/
public class Administrator implements Serializable {

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
    * 管理员名称
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
    * 手机号码,一个手机号只能注册一个账号
    */
    private void setPhone(String phone){
    this.phone = phone;
    }

    /**
    * 管理员的等级
    */
    private void setLevel(Integer level){
    this.level = level;
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
    * 管理员名称
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
    * 手机号码,一个手机号只能注册一个账号
    */
    private String getPhone(){
    return this.phone;
    }

    /**
    * 管理员的等级
    */
    private Integer getLevel(){
    return this.level;
    }

}

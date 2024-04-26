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
* @TableName lost_item
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LostItem implements Serializable {

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
    * 失物名称
    */
    @Size(max= 25,message="编码长度不能超过25")
    @ApiModelProperty("失物名称")
    @Length(max= 25,message="编码长度不能超过25")
    private String name;
    /**
    * 失物图片
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("失物图片")
    @Length(max= 50,message="编码长度不能超过50")
    private String image;
    /**
    * 失物类别
    */
    @ApiModelProperty("失物类别")
    private Integer categoryId;
    /**
    * 丢失地点
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("丢失地点")
    @Length(max= 20,message="编码长度不能超过20")
    private String lostLocation;
    /**
    * 丢失时间
    */
    @NotNull(message="[丢失时间]不能为空")
    @ApiModelProperty("丢失时间")
    private Date lostTime;
    /**
    * 详细描述
    */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("详细描述")
    @Length(max= 100,message="编码长度不能超过100")
    private String description;
    /**
    * 失主姓名
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("失主姓名")
    @Length(max= 20,message="编码长度不能超过20")
    private String ownerName;
    /**
    * 失主手机号码
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("失主手机号码")
    @Length(max= 20,message="编码长度不能超过20")
    private String phone;
    /**
    * 是否已经找到
    */
    @ApiModelProperty("是否已经找到")
    private Integer founded;
    /**
    * 创建这条失物信息的用户id
    */
    @ApiModelProperty("创建这条失物信息的用户id")
    private Integer createUser;

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
    * 失物名称
    */
    private void setName(String name){
    this.name = name;
    }

    /**
    * 失物图片
    */
    private void setImage(String image){
    this.image = image;
    }

    /**
    * 失物类别
    */
    private void setCategoryId(Integer categoryId){
    this.categoryId = categoryId;
    }

    /**
    * 丢失地点
    */
    private void setLostLocation(String lostLocation){
    this.lostLocation = lostLocation;
    }

    /**
    * 丢失时间
    */
    private void setLostTime(Date lostTime){
    this.lostTime = lostTime;
    }

    /**
    * 详细描述
    */
    private void setDescription(String description){
    this.description = description;
    }

    /**
    * 失主姓名
    */
    private void setOwnerName(String ownerName){
    this.ownerName = ownerName;
    }

    /**
    * 失主手机号码
    */
    private void setPhone(String phone){
    this.phone = phone;
    }

    /**
    * 是否已经找到
    */
    private void setFounded(Integer founded){
    this.founded = founded;
    }

    /**
    * 创建这条失物信息的用户id
    */
    private void setCreateUser(Integer createUser){
    this.createUser = createUser;
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
    * 失物名称
    */
    private String getName(){
    return this.name;
    }

    /**
    * 失物图片
    */
    private String getImage(){
    return this.image;
    }

    /**
    * 失物类别
    */
    private Integer getCategoryId(){
    return this.categoryId;
    }

    /**
    * 丢失地点
    */
    private String getLostLocation(){
    return this.lostLocation;
    }

    /**
    * 丢失时间
    */
    private Date getLostTime(){
    return this.lostTime;
    }

    /**
    * 详细描述
    */
    private String getDescription(){
    return this.description;
    }

    /**
    * 失主姓名
    */
    private String getOwnerName(){
    return this.ownerName;
    }

    /**
    * 失主手机号码
    */
    private String getPhone(){
    return this.phone;
    }

    /**
    * 是否已经找到
    */
    private Integer getFounded(){
    return this.founded;
    }

    /**
    * 创建这条失物信息的用户id
    */
    private Integer getCreateUser(){
    return this.createUser;
    }

}

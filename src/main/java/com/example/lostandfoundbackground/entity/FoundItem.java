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
* @TableName found_item
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FoundItem implements Serializable {

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
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
    * 物品名称
    */
    @Size(max= 25,message="编码长度不能超过25")
    @ApiModelProperty("物品名称")
    @Length(max= 25,message="编码长度不能超过25")
    private String name;
    /**
    * 是否已经认领
    */
    @ApiModelProperty("是否已经认领")
    private Integer claimed;
    /**
    * 物品图片
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("物品图片")
    @Length(max= 50,message="编码长度不能超过50")
    private String image;
    /**
    * 物品类别
    */
    @ApiModelProperty("物品类别")
    private Integer categoryId;
    /**
    * 发现地点
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("发现地点")
    @Length(max= 20,message="编码长度不能超过20")
    private String foundLocation;
    /**
    * 发现时间
    */
    @NotNull(message="[发现时间]不能为空")
    @ApiModelProperty("发现时间")
    private Date foundTime;
    /**
    * 详细描述
    */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("详细描述")
    @Length(max= 100,message="编码长度不能超过100")
    private String description;
    /**
    * 姓名
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("姓名")
    @Length(max= 20,message="编码长度不能超过20")
    private String ownerName;
    /**
    * 手机号码
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("手机号码")
    @Length(max= 20,message="编码长度不能超过20")
    private String phone;
    /**
    * 创建这条招领信息的用户id
    */
    @ApiModelProperty("创建这条招领信息的用户id")
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
    * 物品名称
    */
    private void setName(String name){
    this.name = name;
    }

    /**
    * 是否已经认领
    */
    private void setClaimed(Integer claimed){
    this.claimed = claimed;
    }

    /**
    * 物品图片
    */
    private void setImage(String image){
    this.image = image;
    }

    /**
    * 物品类别
    */
    private void setCategoryId(Integer categoryId){
    this.categoryId = categoryId;
    }

    /**
    * 发现地点
    */
    private void setFoundLocation(String foundLocation){
    this.foundLocation = foundLocation;
    }

    /**
    * 发现时间
    */
    private void setFoundTime(Date foundTime){
    this.foundTime = foundTime;
    }

    /**
    * 详细描述
    */
    private void setDescription(String description){
    this.description = description;
    }

    /**
    * 姓名
    */
    private void setOwnerName(String ownerName){
    this.ownerName = ownerName;
    }

    /**
    * 手机号码
    */
    private void setPhone(String phone){
    this.phone = phone;
    }

    /**
    * 创建这条招领信息的用户id
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
    * 物品名称
    */
    private String getName(){
    return this.name;
    }

    /**
    * 是否已经认领
    */
    private Integer getClaimed(){
    return this.claimed;
    }

    /**
    * 物品图片
    */
    private String getImage(){
    return this.image;
    }

    /**
    * 物品类别
    */
    private Integer getCategoryId(){
    return this.categoryId;
    }

    /**
    * 发现地点
    */
    private String getFoundLocation(){
    return this.foundLocation;
    }

    /**
    * 发现时间
    */
    private Date getFoundTime(){
    return this.foundTime;
    }

    /**
    * 详细描述
    */
    private String getDescription(){
    return this.description;
    }

    /**
    * 姓名
    */
    private String getOwnerName(){
    return this.ownerName;
    }

    /**
    * 手机号码
    */
    private String getPhone(){
    return this.phone;
    }

    /**
    * 创建这条招领信息的用户id
    */
    private Integer getCreateUser(){
    return this.createUser;
    }

}

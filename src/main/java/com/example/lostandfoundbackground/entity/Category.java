package com.example.lostandfoundbackground.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 分类表
* @TableName category
*/
public class Category implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Integer categoryId;
    /**
    * 类别的名称
    */
    @Size(max= 10,message="编码长度不能超过10")
    @ApiModelProperty("类别的名称")
    @Length(max= 10,message="编码长度不能超过10")
    private String categoryName;
    /**
    * 类别的别名
    */
    @Size(max= 10,message="编码长度不能超过10")
    @ApiModelProperty("类别的别名")
    @Length(max= 10,message="编码长度不能超过10")
    private String categoryAlias;
    /**
    * 创建用户的id
    */
    @ApiModelProperty("创建用户的id")
    private Integer createUser;
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
    * 
    */
    private void setCategoryId(Integer categoryId){
    this.categoryId = categoryId;
    }

    /**
    * 类别的名称
    */
    private void setCategoryName(String categoryName){
    this.categoryName = categoryName;
    }

    /**
    * 类别的别名
    */
    private void setCategoryAlias(String categoryAlias){
    this.categoryAlias = categoryAlias;
    }

    /**
    * 创建用户的id
    */
    private void setCreateUser(Integer createUser){
    this.createUser = createUser;
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
    * 
    */
    private Integer getCategoryId(){
    return this.categoryId;
    }

    /**
    * 类别的名称
    */
    private String getCategoryName(){
    return this.categoryName;
    }

    /**
    * 类别的别名
    */
    private String getCategoryAlias(){
    return this.categoryAlias;
    }

    /**
    * 创建用户的id
    */
    private Integer getCreateUser(){
    return this.createUser;
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

}

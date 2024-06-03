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
 * @TableName lost_item
*/
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LostItem implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Long id;
    /**
    * 创建时间
    */
    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 更新时间
    */
    @NotNull(message="[更新时间]不能为空")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private Long categoryId;
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
    @NotNull(message="丢失时间不能为空")
    @ApiModelProperty("丢失时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
    private int founded;
    /**
    * 创建这条失物信息的用户id
    */
    @ApiModelProperty("创建这条失物信息的用户id")
    private Long createUser;

}

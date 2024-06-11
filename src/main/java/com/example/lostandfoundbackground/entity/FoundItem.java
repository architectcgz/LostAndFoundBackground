package com.example.lostandfoundbackground.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
 * @TableName found_item
*/
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    private Long createUser;

}

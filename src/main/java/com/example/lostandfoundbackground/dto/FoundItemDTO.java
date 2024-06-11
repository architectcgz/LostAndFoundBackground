package com.example.lostandfoundbackground.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Date;

import static com.example.lostandfoundbackground.constants.RegexPatterns.PHONE_REGEX;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoundItemDTO {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @NotBlank(message = "物品名称不能为空")
    @Size(min = 1,max = 10,message = "物品名称在1-10个字符之间")
    private String name;
    private Integer claimed;
    @URL
    private String image;
    private Long categoryId;
    @NotBlank(message = "发现地点不能为空")
    @Size(min = 1,max = 20,message = "发现地点在1-20个字符之间")
    private String foundLocation;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "丢失时间不能为空")
    private Date foundTime;
    @NotBlank(message = "失物描述不能为空")
    @Size(min = 1,max = 100,message = "失物描述字数在1-100之间")
    private String description;
    @NotBlank(message = "捡得人名称不能为空")
    @Size(min = 1,max = 20,message = "捡得人名称在1-20个字符之间")
    private String ownerName;//失主名称
    @NotBlank(message = "捡得人手机号不能为空")
    @Pattern(regexp = PHONE_REGEX,message = "捡得人手机号格式错误")
    private String phone;
    private Long createUser;//创建的用户id
}

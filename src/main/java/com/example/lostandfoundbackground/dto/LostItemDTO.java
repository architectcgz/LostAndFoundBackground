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
public class LostItemDTO {
    private Long id;
    @NotBlank(message = "失物名称不能为空")
    @Size(min = 1,max = 10,message = "失物名称在1-10个字符之间")
    private String name;
    private Integer founded;//是否已经找到
    @URL
    private String image;
    @NotNull
    private Long categoryId;//分类id
    @NotBlank(message = "丢失地点不能为空")
    @Size(min = 1,max = 20,message = "丢失地点在1-20个字符之间")
    private String lostLocation;//丢失地点
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "丢失时间不能为空")
    private Date lostTime;//发现时间
    @NotBlank(message = "失物描述不能为空")
    @Size(min = 1,max = 100,message = "失物描述字数在1-100之间")
    private String description;
    @NotBlank(message = "失主名称不能为空")
    @Size(min = 1,max = 20,message = "失主名称在1-20个字符之间")
    private String ownerName;//失主名称
    @NotBlank(message = "失主手机号不能为空")
    @Pattern(regexp = PHONE_REGEX,message = "手机号格式错误")
    private String phone;//失主手机号
    private Long createUser;//创建的用户id
}

package com.example.lostandfoundbackground.dto;

import lombok.Data;

/**
 * @author archi
 */
@Data
public class RegisterFormDTO {
    private String name;
    private String phone;
    private String pwd;
    private String repeatPwd;
    private String code;
}

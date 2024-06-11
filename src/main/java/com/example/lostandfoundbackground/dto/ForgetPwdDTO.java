package com.example.lostandfoundbackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author archi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPwdDTO {
    private String phone;
    private String pwd;
    private String repeatPwd;
    private String code;
}

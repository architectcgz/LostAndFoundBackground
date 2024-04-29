package com.example.lostandfoundbackground.dto;

import lombok.Data;

/**
 * @author archi
 */
@Data
public class ChangePwdDTO {
    private String newPwd;
    private String repeatPwd;
}

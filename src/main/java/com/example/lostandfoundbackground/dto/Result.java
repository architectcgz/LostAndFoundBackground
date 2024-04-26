package com.example.lostandfoundbackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author archi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;
    private Long total;

    public static Result ok(){
        return new Result(0, "操作成功", null, null);
    }
    public static Result ok(Object data){
        return new Result(0, "操作成功", data, null);
    }
    public static Result ok(List<?> data, Long total){
        return new Result(0, "操作成功", data, total);
    }
    public static Result fail(String errorMsg){
        return new Result(1, errorMsg, null, null);
    }
}
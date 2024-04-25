package com.example.lostandfoundbackground.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author archi
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResultDTO<T> {
    private Integer code;//业务状态码  0-成功  1-失败
    private String message;//提示信息
    private T data;//响应数据

    //快速返回操作成功响应结果(带响应数据)
    public static <E> ResultDTO<E> success(E data) {
        return new ResultDTO<>(0, "操作成功", data);
    }

}

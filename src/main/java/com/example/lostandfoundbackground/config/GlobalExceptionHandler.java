package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.dto.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author archi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(1,StringUtils.hasLength(e.getMessage())?e.getMessage():"操作失败");
    }
}

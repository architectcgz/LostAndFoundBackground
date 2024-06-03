package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author archi
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e){
        log.info("出现MethodArgumentNotValidException");
        FieldError fieldError = e.getBindingResult().getFieldError();
        String defaultMessage = fieldError.getDefaultMessage();
        return Result.error(1,defaultMessage!=null? defaultMessage :"操作失败");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result dataIntegrityViolationException(DataIntegrityViolationException e){
        log.info(e.getMessage());
        return Result.error(500,"服务器内部错误");
    }

    @ExceptionHandler(Exception.class)
    public Result exception(Exception e){
        log.info(e.getMessage());
        return Result.error(500,"服务器内部错误");
    }
}

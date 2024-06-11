package com.example.lostandfoundbackground.config;

import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.Result;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(BadCredentialsException.class)
    public Result badCredentialsException(BadCredentialsException e){
        log.info(e.getMessage());
        return Result.error(HttpStatus.INTERNAL_ERROR,"服务器内部错误!");
    }

    @ExceptionHandler(RedisCommandExecutionException.class)
    public Result redisCommandExecutionException(RedisCommandExecutionException e){
        log.info(e.getMessage());
        return Result.error(HttpStatus.INTERNAL_ERROR,"服务器内部错误!");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result maxUploadSizeExceededException(MaxUploadSizeExceededException e){
        return Result.error(1,"文件过大,请选择合适的文件!");
    }
}

package com.example.lostandfoundbackground.utils;

import com.example.lostandfoundbackground.dto.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * @author archi
 */
public class ResponseUtil {
    public static void response(HttpServletResponse response, Result result, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(httpStatus.value());
        response.getWriter().write(JsonUtils.javaBeanToJsonString(result));
    }
}

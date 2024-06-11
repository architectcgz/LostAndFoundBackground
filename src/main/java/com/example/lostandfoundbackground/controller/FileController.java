package com.example.lostandfoundbackground.controller;

import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author archi
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/upload/avatar")
    public Result uploadAvatar(MultipartFile file) {
        return fileService.uploadAvatar(file);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/upload/item-image")
    public Result uploadItemImage(MultipartFile file) {
        return fileService.uploadItemImage(file);
    }
}

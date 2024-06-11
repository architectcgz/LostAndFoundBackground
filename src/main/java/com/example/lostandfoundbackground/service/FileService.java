package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.dto.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author archi
 */
public interface FileService {

    Result uploadAvatar(MultipartFile file);

    Result uploadItemImage(MultipartFile file);
}

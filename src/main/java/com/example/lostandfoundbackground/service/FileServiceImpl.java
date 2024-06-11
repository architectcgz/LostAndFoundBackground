package com.example.lostandfoundbackground.service;

import com.example.lostandfoundbackground.constants.HttpStatus;
import com.example.lostandfoundbackground.dto.Result;
import com.example.lostandfoundbackground.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author archi
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService{
    @Override
    public Result uploadAvatar(MultipartFile file) {
        if(file==null){
            return Result.error(1,"上传的文件不能为空!");
        }
        try {
            //把文件的内容存储到阿里云上
            String originalFilename = file.getOriginalFilename();
            //保证文件的名字是唯一的。防止文件被覆盖
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String url = AliOssUtil.uploadFile(fileName, file.getInputStream());
            return Result.ok(url);
        }catch (Exception e){
            log.info(e.getMessage());
            return Result.error(HttpStatus.INTERNAL_ERROR,"服务器问题，上传失败");
        }
    }

    @Override
    public Result uploadItemImage(MultipartFile file) {
        if(file==null){
            return Result.error(1,"上传的文件不能为空!");
        }
        try {
            //把文件的内容存储到阿里云上
            String originalFilename = file.getOriginalFilename();
            //保证文件的名字是唯一的。防止文件被覆盖
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            String url = AliOssUtil.uploadFile(fileName, file.getInputStream());
            return Result.ok(url);
        }catch (Exception e){
            log.info(e.getMessage());
            return Result.error(HttpStatus.INTERNAL_ERROR,"服务器问题，上传失败");
        }
    }
}

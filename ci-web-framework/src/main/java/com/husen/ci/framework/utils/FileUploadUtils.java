package com.husen.ci.framework.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/***
 @Author:MrHuang
 @Date: 2019/7/12 15:37
 @DESC: TODO
 @VERSION: 1.0
 ***/
public class FileUploadUtils {

    public static String getUploadFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }

    public static InputStream getUploadInputStream(MultipartFile file) throws IOException {
        return file.getInputStream();
    }
}

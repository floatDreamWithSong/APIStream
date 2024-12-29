package com.daydreamer.apistream.service.oss;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class Uploader {

    private final MinioUtil minioUtil;

    public Uploader(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }
    public Boolean existJson(String fileName) {
        return minioUtil.existsJson(fileName);
    }

    /**
     * 上传文件
     */
    public void uploadReport(MultipartFile file, String name) {
            minioUtil.upload(file, name);
    }
    public void uploadString(String content, String name) {
        minioUtil.upload(content, name);
    }

    /**
     * 预览文件
     */
    public String preview(String fileName) {
        return minioUtil.getFileUrl(fileName);
    }
    public String getString(String fileName) {
        return minioUtil.getString(fileName);
    }
    /**
     * 下载文件
     */
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.download(response, fileName);
    }

    /**
     * 删除文件
     */
    public void delete(String fileName) {
        minioUtil.delete(fileName);
    }

}
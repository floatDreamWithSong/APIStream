package com.daydreamer.faastest.demo;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author liyh
 */
@RestController
@RequestMapping("/api")
public class UploadController {

    private final MinioUtil minioUtil;

    public UploadController(MinioUtil minioUtil) {
        this.minioUtil = minioUtil;
    }

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload")
    public String uploadReport(MultipartFile[] files) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String rename = UUID.randomUUID().toString();
            minioUtil.upload(file, rename);
        }
        return "上传成功";
    }

    /**
     * 预览文件
     */
    @GetMapping("/preview")
    public String preview(String fileName) {
        return minioUtil.getFileUrl(fileName);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public void download(String fileName, HttpServletResponse response) {
        minioUtil.download(response, fileName);
    }

    /**
     * 删除文件
     */
    @GetMapping("/delete")
    public String delete(String fileName) {
        minioUtil.delete(fileName);
        return "删除成功";
    }

}
package com.daydreamer.faastest.service.oss;

import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class MinioUtil {

    private final MinioClient minioClient;

    private final MinioConfig configuration;

    public MinioUtil(MinioClient minioClient, MinioConfig configuration) {
        this.minioClient = minioClient;
        this.configuration = configuration;
    }

    /**
     * 判断bucket是否存在，不存在则创建
     */
    public boolean existBucket(String bucketName) {
        boolean exists;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                exists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exists = false;
        }
        return exists;
    }

    /**
     * 删除bucket
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名称
     */
    public void upload(MultipartFile file, String fileName) {
        // 使用putObject上传一个文件到存储桶中。
        try(InputStream inputStream = file.getInputStream()){
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void upload(String content, String fileName) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName+".json")
                    .stream(inputStream, bytes.length, -1)
                    .contentType("application/json")
                    .build());
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public String getString(String fileName) {
        String jsonContent = null; // 初始化返回值
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                   .bucket(configuration.getBucketName())
                   .object(fileName + ".json")
                   .build();
            try (InputStream is = minioClient.getObject(getObjectArgs)) {
                // 将InputStream转换为String
                jsonContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonContent; // 返回读取的内容
    }

    /**
     * 获取文件访问地址（有过期时间）
     *
     * @param fileName 文件名称
     * @param time     时间
     * @param timeUnit 时间单位
     */
    public String getExpireFileUrl(String fileName, int time, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .expiry(time, timeUnit).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件访问地址
     *
     * @param fileName 文件名称
     */
    public String getFileUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(configuration.getBucketName())
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     */
    public void download(HttpServletResponse response, String fileName) {
        InputStream in = null;
        try {
            // 获取对象信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 文件下载
            in = minioClient.getObject(GetObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
            IOUtils.copy(in, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     */
    public void delete(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(configuration.getBucketName()).object(fileName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existsJson(String fileName) {
        try {
            StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                    .bucket(configuration.getBucketName())
                    .object(fileName + ".json")
                    .build();
            minioClient.statObject(statObjectArgs); // 如果文件存在，将不会抛出异常
            return true; // 文件存在
        } catch (Exception e) {
            return false; // 文件不存在或其他异常
        }
    }
    
}
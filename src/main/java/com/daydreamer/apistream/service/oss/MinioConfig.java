package com.daydreamer.apistream.service.oss;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String accessKey;

    private String secretKey;

    private String url;

    private String bucketName;

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .region("cn-north-1")
                .endpoint(url)
                .credentials(accessKey,secretKey)
                .build();
    }
}
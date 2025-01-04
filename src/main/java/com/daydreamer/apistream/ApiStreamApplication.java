package com.daydreamer.apistream;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.configuration.SDKConfig;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.ServiceProjectPool;
import com.daydreamer.apistream.service.oss.MinioWorker;
import com.daydreamer.apistream.service.projects.ServiceProjectPoolImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@MapperScan("com.daydreamer.apistream.mapper")
public class ApiStreamApplication {
    private static ApiStreamProjectMapper apiStreamProjectMapper;
    private static APIStreamModuleMapper apiStreamModuleMapper;
    private static MinioWorker minioWorker;
    private static ServiceProjectPool serviceProjectPool;
    @Autowired
    public ApiStreamApplication(APIStreamModuleMapper apiStreamModuleMapper, ApiStreamProjectMapper apiStreamProjectMapper, MinioWorker minioWorker, ServiceProjectPool serviceProjectPool) {
        ApiStreamApplication.apiStreamModuleMapper = apiStreamModuleMapper;
        ApiStreamApplication.apiStreamProjectMapper = apiStreamProjectMapper;
        ApiStreamApplication.serviceProjectPool = serviceProjectPool;
        ApiStreamApplication.minioWorker = minioWorker;
    }

    public static void main(String[] args) {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("--key") && i+1<args.length) {
                SDKConfig.sdkToken = args[++i];
                log.debug("load SDK token: {}", SDKConfig.sdkToken);
            }
        }
        SpringApplication.run(ApiStreamApplication.class, args);
        List<ApiStreamProjectEntity> projects = apiStreamProjectMapper.selectList(null);
        for(ApiStreamProjectEntity project : projects) {
            serviceProjectPool.createProject(project.getProjectName(),project.getProjectId());
            log.debug("recreate project: {}", project.getProjectName());
        }
        List<APIStreamModuleEntity> modules = apiStreamModuleMapper.selectList(null);
        for(APIStreamModuleEntity module : modules) {
            if(minioWorker.existJson(module.getId())){
                AddModuleServiceSDKJsonEntity json = JsonProcessor.gson.fromJson(minioWorker.getString(module.getId()), AddModuleServiceSDKJsonEntity.class);
                serviceProjectPool.reCoverModule(json, UUID.fromString(module.getId()), module.isDisabled());
            }
        }
    }
}

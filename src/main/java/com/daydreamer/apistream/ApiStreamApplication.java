package com.daydreamer.apistream;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.configuration.SDKConfig;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.oss.Uploader;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
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
    private static Uploader uploader;
    @Autowired
    public ApiStreamApplication(APIStreamModuleMapper apiStreamModuleMapper, ApiStreamProjectMapper apiStreamProjectMapper, Uploader uploader) {
        this.apiStreamModuleMapper = apiStreamModuleMapper;
        this.apiStreamProjectMapper = apiStreamProjectMapper;
        this.uploader = uploader;
    }

    public static void main(String[] args) {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("--key") && i+1<args.length) {
                SDKConfig.sdkToken = args[++i];
                log.info("重新启动 SDK token: " + SDKConfig.sdkToken);
            }
        }
        SpringApplication.run(ApiStreamApplication.class, args);
        List<ApiStreamProjectEntity> projects = apiStreamProjectMapper.selectList(null);
        for(ApiStreamProjectEntity project : projects) {
            ServiceProjectPool.instance.createProject(project.getProjectName());
            log.info("恢复项目: " + project.getProjectName());
        }
        List<APIStreamModuleEntity> modules = apiStreamModuleMapper.selectList(null);
        for(APIStreamModuleEntity module : modules) {
            if(uploader.existJson(module.getId().toString())){
                AddModuleServiceSDKJsonEntity json = JsonProcessor.gson.fromJson(uploader.getString(module.getId().toString()), AddModuleServiceSDKJsonEntity.class);
                ServiceProjectPool.instance.reCoverModule(json, UUID.fromString(module.getId()), module.isDisabled());
            }
        }
    }
}

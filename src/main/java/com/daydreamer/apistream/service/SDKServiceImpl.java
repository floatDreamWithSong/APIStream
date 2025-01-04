package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.SDKService;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.daydreamer.apistream.common.ModulePath.resolvePath;

@Slf4j
@Service
public class SDKServiceImpl implements SDKService {

    private final ApiStreamProjectMapper apiStreamProjectMapper;
    private final ServiceProjectPool serviceProjectPool;
    @Autowired
    public SDKServiceImpl(ApiStreamProjectMapper apiStreamProjectMapper, ServiceProjectPool serviceProjectPool) {
        this.apiStreamProjectMapper = apiStreamProjectMapper;
        this.serviceProjectPool = serviceProjectPool;
    }

    @Override
    public UniResponse<Boolean> addModule(AddModuleServiceSDKJsonEntity json) {
        ResolvedPath _path = resolvePath(json.path);
        if(!serviceProjectPool.hasProject(_path.projectName)){
            String msg = "project not exist : "+json.path;
            log.error(msg);
            return new UniResponse<>(1, msg, false);
        }
        serviceProjectPool.insertModule(_path.projectName, json);
        String msg = "deploy module success : "+json.path;
        log.info(msg);
        return new UniResponse<>(0, msg, true);
    }

    @Override
    public UniResponse<Boolean> createProject(String projectName) {
        UUID projectId = serviceProjectPool.createProject(projectName);
        ApiStreamProjectEntity apiStreamProjectEntity = new ApiStreamProjectEntity();
        apiStreamProjectEntity.setProjectName(projectName);
        apiStreamProjectEntity.setProjectId(projectId.toString());
        apiStreamProjectMapper.insert(apiStreamProjectEntity);
        log.info("create project success : {}", projectName);
        return new UniResponse<>(0, "project created", true);
    }

    public void createProject(String projectName, String projectId) {
        serviceProjectPool.createProject(projectName, projectId);
        ApiStreamProjectEntity apiStreamProjectEntity = new ApiStreamProjectEntity();
        apiStreamProjectEntity.setProjectName(projectName);
        apiStreamProjectEntity.setProjectId(projectId);
        apiStreamProjectMapper.insert(apiStreamProjectEntity);
    }

    @Override
    public UniResponse<Boolean> removeProject(String projectName) {
        if(!serviceProjectPool.hasProject(projectName)){
            return new UniResponse<>(1, "project not exist", false);
        }
        serviceProjectPool.removeProject(projectName);
        File file = new File("logs/"+projectName);
        if (file.exists()&&file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(file);
            } catch (IOException e) {

                log.error("delete {} error : {}", projectName, e.getMessage());
                return new UniResponse<>(1, "delete project error", false);
            }
        }
        if(ModulePath.removeLog(projectName)){
            log.debug("remove project success : {}", projectName);
        }
        else{
            log.warn("remove project success but remove log failed : {}", projectName);
        }
        return new UniResponse<>(0, "project removed", true);
    }

    @Override
    public UniResponse<Boolean> existProject(String projectName) {
        if (serviceProjectPool.hasProject(projectName)) {
            return new UniResponse<>(0, "project exist", true);
        }
        return new UniResponse<>(1, "project not exist", false);
    }

    @Override
    public UniResponse<Boolean> overwriteProject(String projectName) {
        if (existProject(projectName).code==0){
            removeProject(projectName);
        }
        return createProject(projectName);
    }
}

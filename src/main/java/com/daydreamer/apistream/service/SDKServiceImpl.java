package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.common.modules.ServiceModule;
import com.daydreamer.apistream.controller.interfaces.SDKService;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.projects.ServiceProject;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.daydreamer.apistream.common.ModulePath.resolvePath;

@Slf4j
@Service
public class SDKServiceImpl implements SDKService {

    private ApiStreamProjectMapper apiStreamProjectMapper;
    private APIStreamModuleMapper apiStreamModuleMapper;
    @Autowired
    public SDKServiceImpl(ApiStreamProjectMapper apiStreamProjectMapper, APIStreamModuleMapper apiStreamModuleMapper) {
        this.apiStreamProjectMapper = apiStreamProjectMapper;
        this.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Override
    public UniResponse addModule(AddModuleServiceSDKJsonEntity json) {
        ResolvedPath _path = resolvePath(json.path);
        if(!ServiceProjectPool.instance.hasProject(_path.projectName)){
            String msg = "project not exist : "+json.path;
            log.error(msg);
            return new UniResponse<>(1, msg);
        }
        UUID id = ServiceProjectPool.instance.insertModule(_path.projectName, json);
        APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
        moduleEntity.setId(id.toString());
        moduleEntity.setDisabled(false);
        apiStreamModuleMapper.insert(moduleEntity);
        String msg = "deploy module success : "+json.path;
        log.info(msg);
        return new UniResponse<>(0, msg);
    }

    @Override
    public UniResponse createProject(String projectName) {
        ServiceProjectPool.instance.createProject(projectName);
        ApiStreamProjectEntity apiStreamProjectEntity = new ApiStreamProjectEntity();
        apiStreamProjectEntity.setProjectName(projectName);
        apiStreamProjectMapper.insert(apiStreamProjectEntity);
        log.info("create project success : {}", projectName);
        return new UniResponse<>(0, "project created");
    }

    @Override
    public UniResponse removeProject(String projectName) {
        if(!ServiceProjectPool.instance.hasProject(projectName)){
            return new UniResponse<>(1, "project not exist");
        }
        ServiceProjectPool.instance.removeProject(projectName);
        log.info("remove project success : {}", projectName);
        return new UniResponse(0, "project removed");
    }

    @Override
    public UniResponse existProject(String projectName) {
        if (ServiceProjectPool.instance.hasProject(projectName)) {
            return new UniResponse<>(0, "project exist");
        }
        return new UniResponse<>(1, "project not exist");
    }

    @Override
    public UniResponse overwriteProject(String projectName) {
        if (existProject(projectName).code==0){
            removeProject(projectName);
        }
        return createProject(projectName);
    }
}

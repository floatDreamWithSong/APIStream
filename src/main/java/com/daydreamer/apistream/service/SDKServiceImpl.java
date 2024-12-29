package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.SDKService;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.daydreamer.apistream.common.ModulePath.resolvePath;

@Slf4j
@Service
public class SDKServiceImpl implements SDKService {



    @Override
    public UniResponse addModule(AddModuleServiceSDKJsonEntity json) {
        ResolvedPath _path = resolvePath(json.path);
        if(!ServiceProjectPool.instance.hasProject(_path.projectName)){
            String msg = "project not exist : "+json.path;
            log.error(msg);
            return new UniResponse(1, msg);
        }
        ServiceProjectPool.instance.insertModule(_path.projectName, json);
        String msg = "deploy module success : "+json.path;
        log.info(msg);
        return new UniResponse(0, msg);
    }

    @Override
    public UniResponse createProject(String projectName) {
        ServiceProjectPool.instance.createProject(projectName);
        return new UniResponse(0, "project created");
    }

    @Override
    public UniResponse removeProject(String projectName) {
        if(!ServiceProjectPool.instance.hasProject(projectName)){
            return new UniResponse(1, "project not exist");
        }
        ServiceProjectPool.instance.removeProject(projectName);
        return new UniResponse(0, "project removed");
    }

    @Override
    public UniResponse existProject(String projectName) {
        if (ServiceProjectPool.instance.hasProject(projectName)) {
            return new UniResponse(0, "project exist");
        }
        return new UniResponse(1, "project not exist");
    }

    @Override
    public UniResponse overwriteProject(String projectName) {
        if (existProject(projectName).code==0){
            removeProject(projectName);
        }
        return createProject(projectName);
    }
}

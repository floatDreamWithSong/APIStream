package com.daydreamer.apistream.service.projects;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.modules.ServiceModule;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.oss.MinioWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Component
public class ServiceProjectPool {
    private static ApiStreamProjectMapper apiStreamProjectMapper;
    private static APIStreamModuleMapper apiStreamModuleMapper;
    private static MinioWorker minioWorker;
    @Autowired
    public void setApiStreamProjectMapper(ApiStreamProjectMapper apiStreamProjectMapper) {
        ServiceProjectPool.apiStreamProjectMapper = apiStreamProjectMapper;
    }
    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        ServiceProjectPool.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Autowired
    public void setUploader(MinioWorker minioWorker) {
        ServiceProjectPool.minioWorker = minioWorker;
    }

    public final static ServiceProjectPool instance = new ServiceProjectPool();
    private final static HashMap<String, ServiceProject> projects = new HashMap<>();

    public void createProject(String projectName) {
        ServiceProject project = new ServiceProject(projectName);
        projects.put(projectName, project);
    }

    public void removeProject(String projectName) {
        if (projects.containsKey(projectName)) {
            ServiceProject project = projects.get(projectName);
            project.modules.forEach((key, value) -> {
                minioWorker.delete(value.getId().toString()+".json");
                apiStreamModuleMapper.deleteById(value.getId().toString());
            });
            project.disabledModules.forEach((key, value) -> {
                minioWorker.delete(value.toString()+".json");
                apiStreamModuleMapper.deleteById(value.toString());
            });
            apiStreamProjectMapper.deleteById(projectName);
            projects.remove(projectName);
        }
    }

    public UUID insertModule(String projectName, AddModuleServiceSDKJsonEntity json) {
        UUID uuid = UUID.randomUUID();
        minioWorker.uploadString(JsonProcessor.gson.toJson(json),uuid.toString());
        APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
        moduleEntity.setId(uuid.toString());
        moduleEntity.setDisabled(false);
        apiStreamModuleMapper.insert(moduleEntity);
        projects.get(projectName).createModule(json, uuid);
        return uuid;
    }
    public void reCoverModule(AddModuleServiceSDKJsonEntity json, UUID uuid, Boolean isDisabled) {
        String projectName = ModulePath.resolvePath(json.path).projectName;
        projects.get(projectName).reCoverModule(json, uuid,isDisabled);
    }

    public boolean disableModule(String modulePath, String projectName) {
        if (!projects.containsKey(projectName)) {
            return false;
        }
        ServiceProject project = projects.get(projectName);
        if (!project.hasModule(modulePath)) {
            return false;
        }
        ServiceModule module = project.modules.get(modulePath);
        APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
        moduleEntity.setId(module.getId().toString());
        moduleEntity.setDisabled(true);
        if(apiStreamModuleMapper.updateById(moduleEntity)==0){
            log.error("更新模块状态失败");
            return false;
        }
        project.disableModule(modulePath);
        return true;
    }
    public boolean enableModule(String modulePath, String projectName) {
        if(!projects.containsKey(projectName)) {
            log.error("项目不存在");
            return false;
        }
        ServiceProject project = projects.get(projectName);
        if(project.hasModule(modulePath)){
            log.error("模块已存在");
            return false;
        }
        if(!project.hasDisabledModule(modulePath)){
            log.error("模块不在禁用列表中");
            return false;
        }
        UUID uuid = project.getDisabledModuleId(modulePath);
        if (!minioWorker.existJson(uuid.toString())){
            log.error("模块不存在bucket中");
            return false;
        }
        APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
        moduleEntity.setId(uuid.toString());
        moduleEntity.setDisabled(false);
        apiStreamModuleMapper.updateById(moduleEntity);
        AddModuleServiceSDKJsonEntity json = JsonProcessor.gson.fromJson(minioWorker.getString(uuid.toString()), AddModuleServiceSDKJsonEntity.class);
        project.createModule(json, uuid);
        return true;
    }

    public boolean removeModule(String projectName, String modulePath) {
        if (!projects.containsKey(projectName))
            return false;
        ServiceProject project = projects.get(projectName);
        UUID moduleId = project.getDisabledModuleId(modulePath);
        if (moduleId != null){
            minioWorker.delete(moduleId+".json");
            apiStreamModuleMapper.deleteById(moduleId.toString());
            project.removeDisabledModule(modulePath);
            return false;
        }
        ServiceModule module = project.removeModule(modulePath);
        if(module == null){
            log.warn("模块不存在");
            return false;
        }
        minioWorker.delete(module.getId().toString()+".json");
        apiStreamModuleMapper.deleteById(module.getId().toString());
        project.removeDisabledModule(modulePath);
        return true;
    }

    public String callModule(String projectName, String modulePath, String fnName, ArrayList<ServiceArgument> args) {
        return projects.get(projectName).callService(modulePath,fnName,args );
    }

    public boolean hasModule(String projectName, String modulePath, String fnName) {
        if (!projects.containsKey(projectName))
            return false;
        return projects.get(projectName).hasService(modulePath,fnName);
    }

    public boolean hasProject(String projectName) {
        return projects.containsKey(projectName);
    }

}


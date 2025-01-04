package com.daydreamer.apistream.service.projects;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.dto.response.ServiceResult;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.modules.ServiceModule;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.ServiceProjectPool;
import com.daydreamer.apistream.service.oss.MinioWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Component
public class ServiceProjectPoolImpl implements ServiceProjectPool {
    private static ApiStreamProjectMapper apiStreamProjectMapper;
    private static APIStreamModuleMapper apiStreamModuleMapper;
    private static MinioWorker minioWorker;
    @Autowired
    public void setApiStreamProjectMapper(ApiStreamProjectMapper apiStreamProjectMapper) {
        ServiceProjectPoolImpl.apiStreamProjectMapper = apiStreamProjectMapper;
    }
    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        ServiceProjectPoolImpl.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Autowired
    public void setUploader(MinioWorker minioWorker) {
        ServiceProjectPoolImpl.minioWorker = minioWorker;
    }

    private final static HashMap<String, ServiceProject> projects = new HashMap<>();

    public UUID createProject(String projectName) {
        if (projects.containsKey(projectName)) {
            log.warn("project already exist");
            return null;
        }
        UUID projectId = UUID.randomUUID();
        ServiceProject project = new ServiceProject(projectName, projectId);
        projects.put(projectName, project);
        return projectId;
    }

    public void createProject(String projectName, String projectId) {
        if (projects.containsKey(projectName)) {
            log.warn("project already exist");
            return;
        }
        ServiceProject project = new ServiceProject(projectName, UUID.fromString(projectId));
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
        moduleEntity.setAvgRuntime(0);
        moduleEntity.setMaxRuntime(0);
        moduleEntity.setMinRuntime(0);
        moduleEntity.setTotalCallTimes(0);
        moduleEntity.setErrorCount(0);
        moduleEntity.setModulePath(ModulePath.resolvePath(json.path).modulePath);
        ServiceProject project = projects.get(projectName);
        moduleEntity.setProjectId(project.getProjectId().toString());
        apiStreamModuleMapper.insert(moduleEntity);
        project.createModule(json, uuid);
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
        UpdateWrapper<APIStreamModuleEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_disabled", true);
        updateWrapper.eq("id", module.getId().toString());
        if(apiStreamModuleMapper.update(updateWrapper)==0){
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
        };
        UpdateWrapper<APIStreamModuleEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_disabled", false);
        updateWrapper.eq("id", uuid.toString());
        if(apiStreamModuleMapper.update(updateWrapper)==0){
            log.error("更新模块状态失败");
            return false;
        }
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
        ModulePath.removeLog(projectName+modulePath+".log");
        return true;
    }

    public ServiceResult callModule(String projectName, String modulePath, String fnName, ArrayList<ServiceArgument> args) {
        return projects.get(projectName).callService(modulePath,fnName,args );
    }

    public boolean hasModule(String projectName, String modulePath, String fnName) {
        if (!projects.containsKey(projectName))
            return false;
        return projects.get(projectName).hasService(modulePath,fnName);
    }

    public UUID getModuleId(String projectName, String modulePath) {
        if (!projects.containsKey(projectName))
            return null;
        return projects.get(projectName).getModuleId(modulePath);
    }

    public boolean hasProject(String projectName) {
        return projects.containsKey(projectName);
    }
    public UUID getProjectId(String projectName) {
        return projects.get(projectName).getProjectId();
    }

}


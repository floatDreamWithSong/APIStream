package com.daydreamer.apistream.service.projects;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.common.modules.ServiceFunction;
import com.daydreamer.apistream.common.modules.ServiceModule;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.oss.Uploader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    private static Uploader uploader;
    @Autowired
    public void setApiStreamProjectMapper(ApiStreamProjectMapper apiStreamProjectMapper) {
        this.apiStreamProjectMapper = apiStreamProjectMapper;
    }
    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        this.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Autowired
    public void setUploader(Uploader uploader) {
        this.uploader = uploader;
    }

    public final static ServiceProjectPool instance = new ServiceProjectPool();
    private final static HashMap<String, ServiceProject> projects = new HashMap<>();
    /**
     * 注册项目
     * @param projectName
     * @return
     */
    public void createProject(String projectName) {
        ServiceProject project = new ServiceProject(projectName);
        projects.put(projectName, project);
    }
    /**
     * 移除项目
     * @param projectName
     */
    public void removeProject(String projectName) {
        if (projects.containsKey(projectName)) {
            ServiceProject project = projects.get(projectName);
            project.modules.forEach((key, value) -> {
                uploader.delete(value.getId().toString()+".json");
                apiStreamModuleMapper.deleteById(value.getId().toString());
            });
            apiStreamProjectMapper.deleteById(projectName);
            projects.remove(projectName);
        }
    }
    /**
     * 向项目中添加服务模块
     * @param projectName
     * @param json
     * @return
     */
    public UUID insertModule(String projectName, AddModuleServiceSDKJsonEntity json) {
        UUID uuid = UUID.randomUUID();
        uploader.uploadString(JsonProcessor.gson.toJson(json),uuid.toString());
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
        if(!project.hasDisabledMoudle(modulePath)){
            log.error("模块不在禁用列表中");
            return false;
        }
        UUID uuid = project.getDisabledModuleId(modulePath);
        if (!uploader.existJson(uuid.toString())){
            log.error("模块不存在bucket中");
            return false;
        }
        AddModuleServiceSDKJsonEntity json = JsonProcessor.gson.fromJson(uploader.getString(uuid.toString()), AddModuleServiceSDKJsonEntity.class);
        project.createModule(json, uuid);
        return true;
    }

    public void removeModule(String projectName, String modulePath) {
        if (!projects.containsKey(projectName))
            return;
        ServiceProject project = projects.get(projectName);
        project.removeModule(modulePath);
        project.removeDisabledModule(modulePath);
    }

    /**
     * 调用指定项目的服务模块下的服务函数
     * @param projectName
     * @param modulePath
     * @param fnName
     * @param args
     * @return
     */
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

    public ServiceProject getProject(String projectName) {
        return projects.get(projectName);
    }
}


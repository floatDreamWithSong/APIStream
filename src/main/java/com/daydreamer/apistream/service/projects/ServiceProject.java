package com.daydreamer.apistream.service.projects;

import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.common.modules.ServiceFunction;
import com.daydreamer.apistream.common.modules.ServiceModule;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class ServiceProject {
    @Getter
    private String projectName;
    public HashMap<String, ServiceModule> modules = new HashMap<>();
    public HashMap<String, UUID> disabledMoudles = new HashMap<>();

    public ServiceProject(String projectName) {
        this.projectName = projectName;
    }

    public boolean hasService(String modulePath, String fnName){
        if(!this.modules.containsKey(modulePath)){
            return false;
        }
        return this.modules.get(modulePath).hasServiceFunction(fnName);
    }
    public boolean hasModule(String modulePath){
        return this.modules.containsKey(modulePath);
    }
    public UUID getDisabledModuleId(String modulePath){
        return this.disabledMoudles.get(modulePath);
    }
    public boolean hasDisabledMoudle(String modulePath){
        return this.disabledMoudles.containsKey(modulePath);
    }
    public void createModule(@NotNull AddModuleServiceSDKJsonEntity json, UUID id) {
        String modulePath = ModulePath.resolvePath(json.path).modulePath;
        log.info("添加模块:{}",modulePath);
        ServiceModule module =  new ServiceModule(modulePath, json.initCode, json.options.MaxConcurrency, id);
        json.functions.forEach(fn->module.addServiceFunction(new ServiceFunction(fn.name,fn.code,fn.args )));
        this.modules.put(modulePath, module);
    }
    public void reCoverModule(@NotNull AddModuleServiceSDKJsonEntity json, UUID id, Boolean isDisabled) {
        log.info("try to recover {}", id.toString());
        String modulePath = ModulePath.resolvePath(json.path).modulePath;
        if(isDisabled){
            this.disabledMoudles.put(modulePath, id);
            log.info("恢复禁用模块:{}",modulePath);
            return;
        }
        log.info("恢复模块:{}",modulePath);
        ServiceModule module =  new ServiceModule(modulePath, json.initCode, json.options.MaxConcurrency, id);
        json.functions.forEach(fn->module.addServiceFunction(new ServiceFunction(fn.name,fn.code,fn.args )));
        this.modules.put(modulePath, module);
    }
    public String callService(String modulePath, String fnName, ArrayList<ServiceArgument> args){
        ServiceModule module = this.modules.get(modulePath);
        return module.runServiceFunction(fnName, args);
    }
    public void removeModule(String modulePath){
        log.info("移除模块:{}",modulePath);
        this.modules.remove(modulePath);
    }
    public void disableModule(String modulePath){
        if(this.modules.containsKey(modulePath)){
            ServiceModule module  = this.modules.remove(modulePath);
            log.info("禁用模块:{}",module.getPath());
           this.disabledMoudles.put(module.getPath(), module.getId());
        }
    }
    public void removeDisabledModule(String modulePath){
        log.info("移除禁用模块:{}",modulePath);
        this.disabledMoudles.remove(modulePath);
    }
}

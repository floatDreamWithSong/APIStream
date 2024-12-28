package com.daydreamer.faastest.service.common;

import com.daydreamer.faastest.common.ModulePath;
import com.daydreamer.faastest.common.modules.ServiceArgument;
import com.daydreamer.faastest.common.modules.ServiceFunction;
import com.daydreamer.faastest.common.modules.ServiceModule;
import com.daydreamer.faastest.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceProjectPool {
    public static ServiceProjectPool instance = new ServiceProjectPool();
    private static HashMap<String, ServiceProject> projects = new HashMap<>();

    /**
     * 注册项目
     * @param projectName
     * @return
     */
    public void createProject(String projectName) {
        projects.put(projectName, new ServiceProject());
    }
    /**
     * 移除项目
     * @param projectName
     */
    public void removeProject(String projectName) {
        if (projects.containsKey(projectName)) {
            projects.remove(projectName);
        }
    }

    /**
     * 向项目中添加服务模块
     * @param projectName
     * @param json
     * @return
     */
    public void insertModule(String projectName, AddModuleServiceSDKJsonEntity json) {
        projects.get(projectName).createModule(json);
    }

    public void removeModule(String projectName, String modulePath) {
        if (!projects.containsKey(projectName))
            return;
        projects.get(projectName).removeModule(modulePath);
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
}

class ServiceProject {
    private HashMap<String, ServiceModule> modules = new HashMap<>();

    public boolean hasService(String modulePath, String fnName){
        if(!this.modules.containsKey(modulePath)){
            return false;
        }
        return this.modules.get(modulePath).hasServiceFunction(fnName);
    }
    public void createModule(AddModuleServiceSDKJsonEntity json){
        String modulePath = ModulePath.resolvePath(json.path).modulePath;
        ServiceModule module =  new ServiceModule(modulePath, json.initCode, json.options.MaxConcurrency);
        json.functions.forEach(fn->module.addServiceFunction(new ServiceFunction(fn.name,fn.code,fn.args )));
        this.modules.put(modulePath, module);
    }
    public String callService(String modulePath, String fnName, ArrayList<ServiceArgument> args){
        ServiceModule module = this.modules.get(modulePath);
        return module.runServiceFunction(fnName, args);
    }
    public void removeModule(String modulePath){
        this.modules.remove(modulePath);
    }
    public void stopModule(String modulePath){

    }
}


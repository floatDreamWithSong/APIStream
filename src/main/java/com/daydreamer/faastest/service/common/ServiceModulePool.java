package com.daydreamer.faastest.service.common;
import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import com.daydreamer.faastest.entity.ServiceModule;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class ServiceModulePool {
    public static ServiceModulePool instance = new ServiceModulePool();
    private HashMap<String, ServiceModule> modules = new HashMap<>();
    public void createModule(AddModuleServiceSDKJsonEntity json){
        ServiceModule module =  new ServiceModule(json.path, json.initCode, json.MaxConcurrency);
        json.functions.forEach(fn->module.addServiceFunction(new ServiceFunction(fn.name,fn.code,fn.args )));
        this.modules.put(json.path, module);
    }

    private ResolvedPath resolvePath(String path){
        String modulePath;
        String functionName;
        int lastIndex = path.lastIndexOf("::");
        modulePath = path.substring(0, lastIndex);
        functionName = path.substring(lastIndex+2);
        return new ResolvedPath(modulePath, functionName);
    }

    public String callModule(String path, ArrayList<ServiceArgument> args){
        // 将path中最后一个"::"分割开来
        ResolvedPath _path = resolvePath(path);
        ServiceModule module = this.modules.get(_path.modulePath);
        return module.runServiceFunction(_path.functionName, args);
    }
    public boolean hasServiceOnPath(String path){
        ResolvedPath _path = resolvePath(path);
        ServiceModule module = this.modules.get(_path.modulePath);
        if(module != null){
            if(module.hasServiceFunction(_path.functionName)){
                return true;
            }
        }
        return false;
    }

}
@AllArgsConstructor
class ResolvedPath{
    public String modulePath;
    public String functionName;
}
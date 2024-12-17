package com.daydreamer.faastest.entity;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.context.JavascriptContext;
import com.daydreamer.faastest.context.JavascriptContextImpl;
import com.daydreamer.faastest.entity.dto.response.ServiceResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ServiceModule {
    @Getter
    private UUID id;
    @Getter
    private String path;
    private HashMap<String, ServiceFunction> functions;
    private final JavascriptContext javascriptContext;

    /** 如果没有环境变量，可以增加并行数量，因为不需要同步js上下文
     * @param path           模块路径
     * @param MaxConcurrency 模块最大并行数量
     * @param initCode       初始化模块环境
     */
    public ServiceModule(String path, String initCode, Integer MaxConcurrency) {
        this.id = UUID.randomUUID();
        this.functions = new HashMap<>();
        this.path = path;
        this.javascriptContext = new JavascriptContextImpl(MaxConcurrency, initCode);
    }

    public void addServiceFunction(ServiceFunction serviceFunction) {
        functions.put(serviceFunction.serviceFunctionName, serviceFunction);
    }

    public void addServiceFunctions(ArrayList<ServiceFunction> serviceFunctions) {
        serviceFunctions.forEach(serviceFunction -> addServiceFunction(serviceFunction));
    }

    public String runServiceFunction(String serviceFunctionName, ArrayList<ServiceArgument> serviceFunctionArguments) {
        if (!functions.containsKey(serviceFunctionName))
            return JsonProcessor.gson.toJson(new ServiceResult());
        ServiceFunction function = functions.get(serviceFunctionName);
        return javascriptContext.callServiceFunction(function.getCallFunctionCode(serviceFunctionArguments));
    }

    public void removeServiceFunction(ServiceFunction serviceFunction) {
        functions.remove(serviceFunction.serviceFunctionName);
    }

    public boolean hasServiceFunction(String serviceFunctionName) {
        return functions.containsKey(serviceFunctionName);
    }
}

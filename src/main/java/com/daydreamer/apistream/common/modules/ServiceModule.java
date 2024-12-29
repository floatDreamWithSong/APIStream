package com.daydreamer.apistream.common.modules;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.context.JavascriptContext;
import com.daydreamer.apistream.common.context.JavascriptContextImpl;
import com.daydreamer.apistream.common.dto.response.ServiceResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ServiceModule {
    @Getter
    private final UUID id;
    @Getter
    private final String path;
    private String initCode;
    private final HashMap<String, ServiceFunction> functions;
    private final JavascriptContext javascriptContext;

    /** 如果没有环境变量，可以增加并行数量，因为不需要同步js上下文
     * @param path           模块路径
     * @param MaxConcurrency 模块最大并行数量
     * @param initCode       初始化模块环境
     */
    public ServiceModule(String path, String initCode, int MaxConcurrency, UUID id) {
        this.id = id;
        this.functions = new HashMap<>();
        this.path = path;
        this.initCode = initCode;
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

    public boolean hasServiceFunction(String serviceFunctionName) {
        return functions.containsKey(serviceFunctionName);
    }
}

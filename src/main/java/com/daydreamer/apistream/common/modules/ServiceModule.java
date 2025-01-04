package com.daydreamer.apistream.common.modules;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.context.JavascriptContext;
import com.daydreamer.apistream.common.context.JavascriptContextImpl;
import com.daydreamer.apistream.common.dto.response.ServiceResult;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ServiceModule {
    @Getter
    private final UUID id;
    @Getter
    private final String path;
    private final HashMap<String, ServiceFunction> functions;
    private final JavascriptContext javascriptContext;

    public ServiceModule(String path, String initCode, int MaxConcurrency, UUID id) {
        this.id = id;
        this.functions = new HashMap<>();
        this.path = path;
        this.javascriptContext = new JavascriptContextImpl(MaxConcurrency, initCode);
    }

    public void addServiceFunction(ServiceFunction serviceFunction) {
        functions.put(serviceFunction.serviceFunctionName, serviceFunction);
    }

    public ServiceResult runServiceFunction(String serviceFunctionName, ArrayList<ServiceArgument> serviceFunctionArguments) {
        if (!functions.containsKey(serviceFunctionName))
            return new ServiceResult();
        ServiceFunction function = functions.get(serviceFunctionName);
        return javascriptContext.callService(function.getCallFunctionCode(serviceFunctionArguments));
    }

    public boolean hasServiceFunction(String serviceFunctionName) {
        return functions.containsKey(serviceFunctionName);
    }
}

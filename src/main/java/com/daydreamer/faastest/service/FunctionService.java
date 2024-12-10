package com.daydreamer.faastest.service;

import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class FunctionService {
    private final HashMap<String, ServiceFunction> servicePool = new HashMap<>() ;
    public boolean isFunctionServicePathValid(String path) {
        return servicePool.containsKey(path);
    }
    private ServiceFunction getFunctionService(String path) {
        return servicePool.get(path);
    }
    public Object useFunctionService(String path, ArrayList<ServiceArgument> arguments){
        ServiceFunction serviceFunction = getFunctionService(path);
        if(serviceFunction != null){
            return serviceFunction.runService(arguments);
        }
        ServiceFunction testServiceFunction = new ServiceFunction("test","console.log(a,b);return `[a,b,${a+b}]`;");
        return testServiceFunction.runService(arguments);
    }
    public void addFunctionService(String path, ServiceFunction function) {
        servicePool.put(path, function);
    }
    public void removeFunctionService(String path) {
        servicePool.remove(path);
    }
    public void updateFunctionService(String path, ServiceFunction service) {
        servicePool.put(path, service);
    }
}

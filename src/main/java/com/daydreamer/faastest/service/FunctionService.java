package com.daydreamer.faastest.service;

import com.daydreamer.faastest.entity.ServiceFunction;

import java.util.HashMap;

public class FunctionService {
    private final HashMap<String, ServiceFunction> servicePool = new HashMap<>() ;
    public boolean isFunctionServicePathValid(String path) {
        return servicePool.containsKey(path);
    }
    public ServiceFunction getFunctionService(String path) {
        return servicePool.get(path);
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

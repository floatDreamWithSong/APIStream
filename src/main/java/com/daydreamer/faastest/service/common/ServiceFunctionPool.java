package com.daydreamer.faastest.service.common;

import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import com.daydreamer.faastest.entity.dto.service.ServiceResult;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceFunctionPool {
    public static HashMap<String, ServiceFunction> serviceFunctionMap;
    public static boolean hasServiceFunctionOnPath(String path) {
        return serviceFunctionMap.containsKey(path);
    }
    public static ServiceResult runServiceFunction(String path, ArrayList<ServiceArgument> arguments){
        if(hasServiceFunctionOnPath(path)){
            return serviceFunctionMap.get(path).runService(arguments);
        }
        return null;
    }
}

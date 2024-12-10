package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.AddFunctionService;
import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceJsonEntity;
import com.daydreamer.faastest.service.common.ServiceFunctionPool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AddFunctionServiceImpl implements AddFunctionService {

    @Override
    public AddFunctionServiceResponseEntity addFunction(AddFunctionServiceJsonEntity json) {
        if(ServiceFunctionPool.hasServiceFunctionOnPath(json.path)) {
            return new AddFunctionServiceResponseEntity(2, "已在同路径部署过API函数");
        }
        ServiceFunction function = new ServiceFunction(json.name, json.code, json.args);
        ServiceFunctionPool.serviceFunctionMap.put(json.path, function);
        return new AddFunctionServiceResponseEntity(0, "部署成功");
    }
}

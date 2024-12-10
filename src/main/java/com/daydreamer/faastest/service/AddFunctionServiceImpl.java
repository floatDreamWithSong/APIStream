package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.AddFunctionService;
import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceJsonEntity;
import org.springframework.stereotype.Service;

@Service
public class AddFunctionServiceImpl implements AddFunctionService {

    @Override
    public AddFunctionServiceResponseEntity addFunction(AddFunctionServiceJsonEntity json) {
        return null;
    }
}

package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.response.AddFunctionServiceResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.receive.AddFunctionServiceJsonEntity;

public interface AddFunctionService {
    public AddFunctionServiceResponseEntity addFunction(AddFunctionServiceJsonEntity json);
}

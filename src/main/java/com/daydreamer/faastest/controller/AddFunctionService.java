package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.AddFunctionServiceJsonEntity;

public interface AddFunctionService {
    public AddFunctionServiceResponseEntity addFunction(AddFunctionServiceJsonEntity json);
}

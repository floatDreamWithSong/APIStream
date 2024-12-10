package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.DeleteFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.DeleteFunctionServiceResponseEntity;

public interface DeleteFunctionService {
    public DeleteFunctionServiceResponseEntity deleteFunctionService(DeleteFunctionServiceJsonEntity json);
}

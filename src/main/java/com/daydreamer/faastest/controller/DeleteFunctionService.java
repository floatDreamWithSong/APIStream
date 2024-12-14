package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.receive.DeleteFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.response.DeleteFunctionServiceResponseEntity;

public interface DeleteFunctionService {
    public DeleteFunctionServiceResponseEntity deleteFunctionService(DeleteFunctionServiceJsonEntity json);
}

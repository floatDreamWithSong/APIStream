package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.DeleteFunctionService;
import com.daydreamer.faastest.entity.dto.manage.receive.DeleteFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.response.DeleteFunctionServiceResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteFunctionServiceImpl implements DeleteFunctionService {
    @Override
    public DeleteFunctionServiceResponseEntity deleteFunctionService(DeleteFunctionServiceJsonEntity json) {
        return null;
    }
}

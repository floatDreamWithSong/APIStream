package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.UpdateFunctionService;
import com.daydreamer.faastest.entity.dto.manage.UpdateFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.UpdateFunctionServiceResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateFunctionServiceImpl implements UpdateFunctionService {
    @Override
    public UpdateFunctionServiceResponseEntity updateFunction(UpdateFunctionServiceJsonEntity json) {
        return null;
    }
}

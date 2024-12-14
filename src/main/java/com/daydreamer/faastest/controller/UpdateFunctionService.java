package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.receive.UpdateFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.response.UpdateFunctionServiceResponseEntity;

public interface UpdateFunctionService {
    public UpdateFunctionServiceResponseEntity updateFunction (UpdateFunctionServiceJsonEntity json);
}

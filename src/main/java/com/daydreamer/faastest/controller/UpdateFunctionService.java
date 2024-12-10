package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.UpdateFunctionServiceJsonEntity;
import com.daydreamer.faastest.entity.dto.manage.UpdateFunctionServiceResponseEntity;

public interface UpdateFunctionService {
    public UpdateFunctionServiceResponseEntity updateFunction (UpdateFunctionServiceJsonEntity json);
}

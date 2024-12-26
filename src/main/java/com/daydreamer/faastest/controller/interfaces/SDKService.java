package com.daydreamer.faastest.controller.interfaces;

import com.daydreamer.faastest.entity.dto.info.ModuleInfo;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;

import java.util.ArrayList;
import java.util.UUID;

public interface SDKService {
    UniResponse addModule(AddModuleServiceSDKJsonEntity json);
    UniResponse deleteModule(UUID moduleId);
    UniResponse deleteAllModules();
}

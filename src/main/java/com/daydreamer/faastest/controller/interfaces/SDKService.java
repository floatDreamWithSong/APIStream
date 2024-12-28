package com.daydreamer.faastest.controller.interfaces;

import com.daydreamer.faastest.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.common.dto.response.UniResponse;

public interface SDKService {
    UniResponse addModule(AddModuleServiceSDKJsonEntity json);
    UniResponse createProject(String projectName);
    UniResponse removeProject(String projectName);
    UniResponse existProject(String projectName);
    UniResponse overwriteProject(String projectName);
}

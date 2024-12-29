package com.daydreamer.apistream.controller.interfaces;

import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.apistream.common.dto.response.UniResponse;

public interface SDKService {
    UniResponse addModule(AddModuleServiceSDKJsonEntity json);
    UniResponse createProject(String projectName);
    UniResponse removeProject(String projectName);
    UniResponse existProject(String projectName);
    UniResponse overwriteProject(String projectName);
}

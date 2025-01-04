package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.dto.response.ServiceResult;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.common.dto.receive.sdk.AddModuleServiceSDKJsonEntity;

import java.util.ArrayList;
import java.util.UUID;

public interface ServiceProjectPool {

    UUID createProject(String projectName);

    void createProject(String projectName, String projectId);

    void removeProject(String projectName);

    UUID insertModule(String projectName, AddModuleServiceSDKJsonEntity json);
    void reCoverModule(AddModuleServiceSDKJsonEntity json, UUID uuid, Boolean isDisabled);

    boolean disableModule(String modulePath, String projectName);
    boolean enableModule(String modulePath, String projectName);

    boolean removeModule(String projectName, String modulePath);

    ServiceResult callModule(String projectName, String modulePath, String fnName, ArrayList<ServiceArgument> args);

    boolean hasModule(String projectName, String modulePath, String fnName);

    UUID getModuleId(String projectName, String modulePath);

    boolean hasProject(String projectName);
    UUID getProjectId(String projectName);

}


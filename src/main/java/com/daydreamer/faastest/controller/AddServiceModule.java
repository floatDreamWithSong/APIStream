package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;

public interface AddServiceModule {
    UniResponse addModule(AddModuleServiceSDKJsonEntity json);
}

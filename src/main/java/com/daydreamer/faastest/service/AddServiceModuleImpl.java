package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.AddServiceModule;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import com.daydreamer.faastest.service.common.ServiceModulePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddServiceModuleImpl implements AddServiceModule {

    @Override
    public UniResponse addModule(AddModuleServiceSDKJsonEntity json) {
        ServiceModulePool.instance.createModule(json);
        String msg = "deploy module success : "+json.path;
        log.info(msg);
        return new UniResponse(0, msg);
    }
}

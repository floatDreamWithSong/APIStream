package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.interfaces.SDKService;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import com.daydreamer.faastest.service.common.ServiceModulePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class SDKServiceImpl implements SDKService {

    @Override
    public UniResponse addModule(AddModuleServiceSDKJsonEntity json) {
        ServiceModulePool.instance.createModule(json);
        String msg = "deploy module success : "+json.path;
        log.info(msg);
        return new UniResponse(0, msg);
    }

    @Override
    public UniResponse removeModule(UUID moduleId) {
        return null;
    }

    @Override
    public UniResponse queryModuleList(int pageSize, int PageNumber) {
        return null;
    }

}

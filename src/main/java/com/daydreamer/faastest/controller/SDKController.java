package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.controller.interfaces.SDKService;
import com.daydreamer.faastest.controller.interfaces.UseService;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class SDKController {

    private SDKService serviceModule;

    /**
     * 用于模块部署的服务接口，只负责添加并部署模块
     * 状态：已完成
     * @param body
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleServiceSDK")
    public UniResponse addServiceModuleSDK(@RequestBody AddModuleServiceSDKJsonEntity body) {
        return serviceModule.addModule(body);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/APIStreamModuleServiceSDK")
    public UniResponse deleteServiceModuleSDK(@RequestParam(name = "module_id", required = true) String moduleId) {
        return serviceModule.deleteModule(UUID.fromString(moduleId));
    }

}
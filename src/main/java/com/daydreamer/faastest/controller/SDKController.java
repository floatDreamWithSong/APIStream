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

    private UseService useServiceModule;
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

    /**
     * 用于模块调用的服务接口，只负责调用模块并返回计算结果
     * 状态：已完成
     * @param request
     * @param body
     * @param dynamic_path
     * @return
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{dynamic_path:^(?!APIStreamModuleServiceSDK|APIStreamStaticResources).*}/**")
    public String handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body, @PathVariable String dynamic_path) {
        log.debug("service module: {}",dynamic_path);
        log.debug("body: {}",JsonProcessor.gson.toJson(body));
        return useServiceModule.useServiceFunction(request, body);
    }

}
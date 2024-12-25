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

@Slf4j
@AllArgsConstructor
@RestController
public class SDKController {

    private UseService useServiceModule;
    private SDKService serviceModule;

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleServiceSDK")
    public UniResponse addServiceModuleSDK(@RequestBody AddModuleServiceSDKJsonEntity body) {
        return serviceModule.addModule(body);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{dynamic_path:^(?!APIStreamModuleServiceSDK|APIStreamStaticResources).*}/**")
    public String handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body, @PathVariable String dynamic_path) {
        log.info("service module: {}",dynamic_path);
        log.info("body: {}",JsonProcessor.gson.toJson(body));
        log.info("token: {}",request.getHeader("Authorization"));
        return useServiceModule.useServiceFunction(request, body);
    }

}
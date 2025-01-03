package com.daydreamer.apistream.controller;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.controller.interfaces.UseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
public class ServiceController {
    private UseService useServiceModule;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{dynamic_path:^(?!APIStreamModuleServiceSDK|APIStreamStaticResources).*}/**")
    public String handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body, @PathVariable String dynamic_path) {
        log.debug("service module: {}",dynamic_path);
        log.debug("body: {}", JsonProcessor.gson.toJson(body));
        return useServiceModule.useServiceFunction(request, body);
    }
}

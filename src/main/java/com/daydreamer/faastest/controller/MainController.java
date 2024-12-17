package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
public class MainController {

    private UseServiceModule useServiceModule;
    private AddServiceModule addServiceModule;

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleServiceSDK")
    public UniResponse addServiceModuleSDK(@RequestBody AddModuleServiceSDKJsonEntity body) {
        return addServiceModule.addModule(body);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/**")
    public String handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return useServiceModule.useServiceFunction(request, body);
    }
    /**
     * 由于Get方法没有Body，解析Body会报错，所以要单独拿出来，这里需要抽离公共逻辑，再做一层控制器
     */
//    @CrossOrigin(origins = "*")
//    @GetMapping(value = "/**")
//    public String handleGetRequest(HttpServletRequest request) {
//        return functionService.useServiceFunction(request, null);
//    }
}
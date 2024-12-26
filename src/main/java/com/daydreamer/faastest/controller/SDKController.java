package com.daydreamer.faastest.controller;
import com.daydreamer.faastest.controller.interfaces.SDKService;
import com.daydreamer.faastest.entity.dto.receive.sdk.AddModuleServiceSDKJsonEntity;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@AllArgsConstructor
@RestController
public class SDKController {

    private SDKService serviceModule;

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleServiceSDK")
    public UniResponse addServiceModuleSDK(@RequestBody AddModuleServiceSDKJsonEntity body) {
        return serviceModule.addModule(body);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamProjectServiceSDK")
    public UniResponse createServiceProjectSDK(@RequestParam(name = "project", required = true) String projectName) {
        return serviceModule.createProject(projectName);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/APIStreamProjectServiceSDK")
    public UniResponse deleteServiceProjectSDK(@RequestParam(name = "project", required = true) String projectName) {
        return serviceModule.removeProject(projectName);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamProjectServiceSDK")
    public UniResponse queryServiceProjectSDK(@RequestParam(name = "project", required = true) String projectName) {
        return serviceModule.existProject(projectName);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/APIStreamProjectServiceSDK")
    public UniResponse updateServiceProjectSDK(@RequestParam(name = "project", required = true) String projectName) {
        return serviceModule.overwriteProject(projectName);
    }
}
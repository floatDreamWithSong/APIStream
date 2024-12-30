package com.daydreamer.apistream.controller;

import com.daydreamer.apistream.common.dto.receive.web.DisableAndEnableJson;
import com.daydreamer.apistream.controller.interfaces.WebService;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
public class WebController {
    private WebService webService;

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamProjectQueryService")
    public UniResponse queryProject() {
        return webService.queryProject();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleQueryService")
    public UniResponse queryModule(@RequestParam(name = "project_id") String projectId) {
        return webService.queryModule(projectId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleDetailQueryService")
    public UniResponse queryModuleDetail(@RequestParam(name = "module_id") String moduleId) {
        return webService.queryModuleDetail(moduleId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleDisableService")
    public UniResponse disableModule(@RequestBody DisableAndEnableJson disableAndEnableJson) {
        return webService.disableModule(disableAndEnableJson.module_path, disableAndEnableJson.project_name);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamModuleEnableService")
    public UniResponse enableModule(@RequestBody DisableAndEnableJson disableAndEnableJson) {
        return webService.enableModule(disableAndEnableJson.module_path, disableAndEnableJson.project_name);
    }
}

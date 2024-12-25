package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.controller.interfaces.WebService;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public UniResponse queryModule(@RequestParam(name = "project_id", required = true) String projectId) {
        return webService.queryModule(projectId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleDetailQueryService")
    public UniResponse queryModuleDetail(@RequestParam(name = "module_id", required = true) String moduleId) {
        return webService.queryModuleDetail(moduleId);
    }
}

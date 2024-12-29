package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.common.dto.receive.web.DisableAndEnableJson;
import com.daydreamer.faastest.controller.interfaces.WebService;
import com.daydreamer.faastest.common.dto.response.UniResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Slf4j
@RestController
public class WebController {
    private WebService webService;

    /**
     * 查询项目列表
     * TODO
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamProjectQueryService")
    public UniResponse queryProject() {
        return webService.queryProject();
    }

    /**
     * 查询模块列表
     * TODO
     * @param projectId
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleQueryService")
    public UniResponse queryModule(@RequestParam(name = "project_id", required = true) String projectId) {
        return webService.queryModule(projectId);
    }

    /**
     * 查询模块详情
     * TODO
     * @param moduleId
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleDetailQueryService")
    public UniResponse queryModuleDetail(@RequestParam(name = "module_id", required = true) String moduleId) {
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

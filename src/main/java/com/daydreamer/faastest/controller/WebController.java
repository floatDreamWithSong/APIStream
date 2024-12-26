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

    /**
     * 查询项目列表
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamProjectQueryService")
    public UniResponse queryProject() {
        return webService.queryProject();
    }

    /**
     * 查询模块列表
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
     * @param moduleId
     * @return
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamModuleDetailQueryService")
    public UniResponse queryModuleDetail(@RequestParam(name = "module_id", required = true) String moduleId) {
        return webService.queryModuleDetail(moduleId);
    }
}

package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.interfaces.WebService;
import com.daydreamer.faastest.common.dto.response.ListItem;
import com.daydreamer.faastest.common.dto.response.ModuleDetail;
import com.daydreamer.faastest.common.dto.response.UniResponse;
import com.daydreamer.faastest.service.projects.ServiceProjectPool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebServiceImpl implements WebService {
    /**
     * TODO
     * @return
     */
    @Override
    public UniResponse<List<ListItem>> queryProject() {
        return null;
    }

    /**
     * todo
     * @param projectId
     * @return
     */
    @Override
    public UniResponse<List<ListItem>> queryModule(String projectId) {
        return null;
    }

    /**
     * TODO
     * @param moduleId
     * @return
     */
    @Override
    public UniResponse<ModuleDetail> queryModuleDetail(String moduleId) {
        return null;
    }

    @Override
    public UniResponse<Boolean> disableModule(String modulePath, String projectName) {
        if(!ServiceProjectPool.instance.disableModule(modulePath, projectName)){
            return new UniResponse<>(1, "disable module failed", false);
        }
        return new UniResponse<>(0, "disable module success", true);
    }

    @Override
    public UniResponse<Boolean> enableModule(String modulePath, String projectName) {
        if(!ServiceProjectPool.instance.enableModule(modulePath, projectName)){
            return new UniResponse<>(1, "enable module failed", false);
        }
        return new UniResponse<>(0, "enable module success", true);
    }
}

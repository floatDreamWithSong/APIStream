package com.daydreamer.apistream.service;

import com.daydreamer.apistream.controller.interfaces.WebService;
import com.daydreamer.apistream.common.dto.response.ListItem;
import com.daydreamer.apistream.common.dto.response.ModuleDetail;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebServiceImpl implements WebService {

    @Override
    public UniResponse<List<ListItem>> queryProject() {
        return null;
    }

    @Override
    public UniResponse<List<ListItem>> queryModule(String projectId) {
        return null;
    }

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

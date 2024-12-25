package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.interfaces.WebService;
import com.daydreamer.faastest.entity.dto.response.ListItem;
import com.daydreamer.faastest.entity.dto.response.ModuleDetail;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
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
}

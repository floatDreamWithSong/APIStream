package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.interfaces.WebService;
import com.daydreamer.faastest.common.dto.response.ListItem;
import com.daydreamer.faastest.common.dto.response.ModuleDetail;
import com.daydreamer.faastest.common.dto.response.UniResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

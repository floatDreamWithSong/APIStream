package com.daydreamer.apistream.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daydreamer.apistream.controller.interfaces.WebService;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.service.projects.ServiceProject;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WebServiceImpl implements WebService {

    private ApiStreamProjectMapper apiStreamProjectMapper;
    private APIStreamModuleMapper apiStreamModuleMapper;

    @Autowired
    public WebServiceImpl(ApiStreamProjectMapper apiStreamProjectMapper, APIStreamModuleMapper apiStreamModuleMapper) {
        this.apiStreamProjectMapper = apiStreamProjectMapper;
        this.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Override
    public UniResponse<List<ApiStreamProjectEntity>> queryProject() {
        return new UniResponse<>(0, "query project success", apiStreamProjectMapper.selectList(null));
    }

    @Override
    public UniResponse<List<APIStreamModuleEntity>> queryModule(String projectName) {
        QueryWrapper<APIStreamModuleEntity> queryWrapper = new QueryWrapper<>();
        UUID project_id = ServiceProjectPool.instance.getProjectId(projectName);
        queryWrapper.eq("project_id", project_id.toString());
        return new UniResponse<>(0, "query module success", apiStreamModuleMapper.selectList(queryWrapper));
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

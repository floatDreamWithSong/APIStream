package com.daydreamer.apistream.service;

import com.daydreamer.apistream.controller.interfaces.WebService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daydreamer.apistream.common.dto.response.ApiEndpoint;
import com.daydreamer.apistream.common.dto.response.ListItem;
import com.daydreamer.apistream.common.dto.response.ModuleDetail;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiEndpointEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import com.daydreamer.apistream.entity.ModuleDetailEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.mapper.ApiEndpointMapper;
import com.daydreamer.apistream.mapper.ApiStreamProjectMapper;
import com.daydreamer.apistream.mapper.ModuleDetailMapper;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class WebServiceImpl implements WebService {

    private ApiStreamProjectMapper apiStreamProjectMapper;
    private APIStreamModuleMapper apiStreamModuleMapper;
    private ModuleDetailMapper moduleDetailMapper;
    private ApiEndpointMapper apiEndpointMapper;

    @Autowired
    public WebServiceImpl(
        ApiStreamProjectMapper apiStreamProjectMapper, 
        APIStreamModuleMapper apiStreamModuleMapper,
        ModuleDetailMapper moduleDetailMapper,         
        ApiEndpointMapper apiEndpointMapper           
    ) {
        this.apiStreamProjectMapper = apiStreamProjectMapper;
        this.apiStreamModuleMapper = apiStreamModuleMapper;
        this.moduleDetailMapper = moduleDetailMapper; 
        this.apiEndpointMapper = apiEndpointMapper;   
    }

    @Override
    public UniResponse<List<ApiStreamProjectEntity>> queryProject() {
        return new UniResponse<>(0, "query project success", apiStreamProjectMapper.selectList(null));
    }

    @Override
    public UniResponse<List<APIStreamModuleEntity>> queryModule(String projectId) {
        QueryWrapper<APIStreamModuleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_name", projectId);
        return new UniResponse<>(0, "query module success",
                apiStreamModuleMapper.selectList(queryWrapper));
    }

    @Override
    public UniResponse<ModuleDetail> queryModuleDetail(String moduleId) {
        // 1. 查询模块基本信息
        APIStreamModuleEntity module = apiStreamModuleMapper.selectById(moduleId);
        if (module == null) {
            return new UniResponse<>(1, "module not found", null);
        }

        // 2. 查询模块详细信息(需要添加ModuleDetailMapper)
        ModuleDetailEntity moduleDetail = moduleDetailMapper.selectById(moduleId);
        if (moduleDetail == null) {
            return new UniResponse<>(1, "module detail not found", null);
        }

        // 3. 查询该模块的所有API接口(需要添加ApiEndpointMapper)
        List<ApiEndpointEntity> endpoints = apiEndpointMapper.selectByModuleId(moduleId);

        // 4. 组装ModuleDetail对象
        ModuleDetail detail = new ModuleDetail();
        detail.setId(moduleId);
        detail.setName(moduleDetail.getName());
        detail.setDescription(moduleDetail.getDescription());
        detail.setPath(moduleDetail.getPath());
        detail.setDisabled(module.isDisabled());
        detail.setProjectName(moduleDetail.getProjectName());
        detail.setEndpoints(convertToEndpointDTOs(endpoints));

        return new UniResponse<>(0, "query detail success", detail);
    }

    private List<ApiEndpoint> convertToEndpointDTOs(List<ApiEndpointEntity> entities) {
        return entities.stream()
            .map(entity -> {
                ApiEndpoint endpoint = new ApiEndpoint();
                endpoint.setId(entity.getId());
                endpoint.setPath(entity.getPath());
                endpoint.setMethod(entity.getMethod());
                endpoint.setDescription(entity.getDescription());
                return endpoint;
            })
            .collect(Collectors.toList());
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

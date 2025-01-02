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
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
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
        APIStreamModuleEntity module = apiStreamModuleMapper.selectById(moduleId);
        if (module == null) {
            return new UniResponse<>(1, "module not found", null);
        }

        ModuleDetailEntity moduleDetail = moduleDetailMapper.selectById(moduleId);
        if (moduleDetail == null) {
            return new UniResponse<>(1, "module detail not found", null);
        }

        QueryWrapper<ApiEndpointEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("module_id", moduleId);
        List<ApiEndpointEntity> endpoints = apiEndpointMapper.selectList(wrapper);

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
        try {
            QueryWrapper<APIStreamModuleEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("path", modulePath)
                    .eq("project_name", projectName);

            APIStreamModuleEntity module = apiStreamModuleMapper.selectOne(wrapper);
            if (module == null) {
                return new UniResponse<>(1, "module not found", false);
            }

            module.setDisabled(true);
            apiStreamModuleMapper.updateById(module);

            log.info("Module disabled: {} in project {}", modulePath, projectName);
            return new UniResponse<>(0, "disable module success", true);
        } catch (Exception e) {
            log.error("Failed to disable module", e);
            return new UniResponse<>(1, "disable module failed: " + e.getMessage(), false);
        }
    }

    @Override
    public UniResponse<Boolean> enableModule(String modulePath, String projectName) {
        try {
            QueryWrapper<APIStreamModuleEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("path", modulePath)
                    .eq("project_name", projectName);

            APIStreamModuleEntity module = apiStreamModuleMapper.selectOne(wrapper);
            if (module == null) {
                return new UniResponse<>(1, "module not found", false);
            }

            module.setDisabled(false);
            apiStreamModuleMapper.updateById(module);

            log.info("Module enabled: {} in project {}", modulePath, projectName);
            return new UniResponse<>(0, "enable module success", true);
        } catch (Exception e) {
            log.error("Failed to enable module", e);
            return new UniResponse<>(1, "enable module failed: " + e.getMessage(), false);
        }
    }

    @Override
    public UniResponse<Boolean> createModule(ModuleDetail moduleDetail) {
        try {
            // 1. Create and save module entity
            APIStreamModuleEntity module = new APIStreamModuleEntity();
            module.setId(UUID.randomUUID().toString());
            module.setDisabled(false);
            apiStreamModuleMapper.insert(module);

            // 2. Create and save module detail
            ModuleDetailEntity detail = new ModuleDetailEntity();
            detail.setId(module.getId());
            detail.setName(moduleDetail.getName());
            detail.setDescription(moduleDetail.getDescription());
            detail.setPath(moduleDetail.getPath());
            detail.setProjectName(moduleDetail.getProjectName());
            moduleDetailMapper.insert(detail);

            // 3. Create and save endpoints
            if (moduleDetail.getEndpoints() != null) {
                moduleDetail.getEndpoints().forEach(endpoint -> {
                    ApiEndpointEntity endpointEntity = new ApiEndpointEntity();
                    endpointEntity.setId(UUID.randomUUID().toString());
                    endpointEntity.setModuleId(module.getId());
                    endpointEntity.setPath(endpoint.getPath());
                    endpointEntity.setMethod(endpoint.getMethod());
                    endpointEntity.setDescription(endpoint.getDescription());
                    apiEndpointMapper.insert(endpointEntity);
                });
            }

            return new UniResponse<>(0, "create module success", true);
        } catch (Exception e) {
            log.error("create module failed", e);
            return new UniResponse<>(1, "create module failed: " + e.getMessage(), false);
        }
    }

    @Override
    public UniResponse<Boolean> updateModule(ModuleDetail moduleDetail) {
        try {
            // 1. Update module detail
            ModuleDetailEntity detail = new ModuleDetailEntity();
            detail.setId(moduleDetail.getId());
            detail.setName(moduleDetail.getName());
            detail.setDescription(moduleDetail.getDescription());
            detail.setPath(moduleDetail.getPath());
            detail.setProjectName(moduleDetail.getProjectName());
            moduleDetailMapper.updateById(detail);

            // 2. Delete existing endpoints
            QueryWrapper<ApiEndpointEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("module_id", moduleDetail.getId());
            apiEndpointMapper.delete(wrapper);

            // 3. Create new endpoints
            if (moduleDetail.getEndpoints() != null) {
                moduleDetail.getEndpoints().forEach(endpoint -> {
                    ApiEndpointEntity endpointEntity = new ApiEndpointEntity();
                    endpointEntity.setId(UUID.randomUUID().toString());
                    endpointEntity.setModuleId(moduleDetail.getId());
                    endpointEntity.setPath(endpoint.getPath());
                    endpointEntity.setMethod(endpoint.getMethod());
                    endpointEntity.setDescription(endpoint.getDescription());
                    apiEndpointMapper.insert(endpointEntity);
                });
            }

            return new UniResponse<>(0, "update module success", true);
        } catch (Exception e) {
            log.error("update module failed", e);
            return new UniResponse<>(1, "update module failed: " + e.getMessage(), false);
        }
    }
}

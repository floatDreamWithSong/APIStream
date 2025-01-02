package com.daydreamer.apistream.controller.interfaces;

import com.daydreamer.apistream.common.dto.response.ModuleDetail;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.entity.ApiStreamProjectEntity;
import java.util.List;

public interface WebService {
    // 明确返回类型为项目列表
    UniResponse<List<ApiStreamProjectEntity>> queryProject();

    // 明确返回类型为模块列表
    UniResponse<List<APIStreamModuleEntity>> queryModule(String projectId);

    // 明确返回类型为模块详情
    UniResponse<ModuleDetail> queryModuleDetail(String moduleId);

    // 为方法添加JavaDoc注释
    /**
     * 禁用指定模块
     * @param modulePath 模块路径
     * @param projectName 项目名称
     * @return 操作结果
     */
    UniResponse<Boolean> disableModule(String modulePath, String projectName);

    /**
     * 启用指定模块
     * @param modulePath 模块路径
     * @param projectName 项目名称
     * @return 操作结果
     */
    UniResponse<Boolean> enableModule(String modulePath, String projectName);

    /**
     * 创建新模块
     * @param moduleDetail 模块详情
     * @return 操作结果
     */
    UniResponse<Boolean> createModule(ModuleDetail moduleDetail);

    /**
     * 更新现有模块
     * @param moduleDetail 模块详情
     * @return 操作结果
     */
    UniResponse<Boolean> updateModule(ModuleDetail moduleDetail);
}
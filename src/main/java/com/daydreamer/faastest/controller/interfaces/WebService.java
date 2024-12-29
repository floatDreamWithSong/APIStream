package com.daydreamer.faastest.controller.interfaces;

import com.daydreamer.faastest.common.dto.response.ListItem;
import com.daydreamer.faastest.common.dto.response.ModuleDetail;
import com.daydreamer.faastest.common.dto.response.UniResponse;
import java.util.List;

/**
 * 用于打包在开发工具中的后台系统的控制器
 */
public interface WebService {
    /**
     * 查询所有的项目列表
     * @return
     */
    UniResponse<List<ListItem>> queryProject();

    /**
     * 查询指定项目下的模块列表
     * @param projectId
     * @return
     */
    UniResponse<List<ListItem>> queryModule(String projectId);

    /**
     * 查询指定模块的详细信息
     * @param moduleId
     * @return
     */
    UniResponse<ModuleDetail> queryModuleDetail(String moduleId);

    UniResponse<Boolean> disableModule(String modulePath, String projectName);
    UniResponse<Boolean> enableModule(String modulePath, String projectName);
}

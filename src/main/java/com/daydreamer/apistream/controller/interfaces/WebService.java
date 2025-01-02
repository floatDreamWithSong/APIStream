package com.daydreamer.apistream.controller.interfaces;

import com.daydreamer.apistream.common.dto.response.ListItem;
import com.daydreamer.apistream.common.dto.response.ModuleDetail;
import com.daydreamer.apistream.common.dto.response.UniResponse;
import java.util.List;

/**
 * 用于打包在开发工具中的后台系统的控制器
 */
public interface WebService {
    /**
     * 查询所有的项目列表
     * @return
     */
    UniResponse queryProject();

    /**
     * 查询指定项目下的模块列表
     * @param projectId
     * @return
     */
    UniResponse queryModule(String projectId);

    UniResponse<Boolean> disableModule(String modulePath, String projectName);
    UniResponse<Boolean> enableModule(String modulePath, String projectName);
}

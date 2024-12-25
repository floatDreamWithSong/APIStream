package com.daydreamer.faastest.controller.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface UseService {
    /**
     * 调用用户部署的服务模块
     * @param request
     * @param body
     * @return
     */
    String useServiceFunction(HttpServletRequest request, Map<String, Object> body);
}

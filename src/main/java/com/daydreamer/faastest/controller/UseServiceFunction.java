package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface UseServiceFunction {
    public String useServiceFunction(HttpServletRequest request, Map<String, Object> body);
}

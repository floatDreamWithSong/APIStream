package com.daydreamer.faastest.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface UseServiceModule {
    String useServiceFunction(HttpServletRequest request, Map<String, Object> body);
}

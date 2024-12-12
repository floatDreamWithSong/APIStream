package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.UseServiceFunction;
import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import com.daydreamer.faastest.service.common.ServiceFunctionPool;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class UseServiceFunctionImpl implements UseServiceFunction {

    @Override
    public String useServiceFunction(HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        String query = request.getQueryString();
        String requestMethod = request.getMethod();
        System.out.println("path: " + path);
        System.out.println("userAgent: " + userAgent);
        System.out.println("ip: " + ip);
        System.out.println("query: " + query);
        System.out.println("requestMethod: " + requestMethod);
        System.out.println("body: ");
        if (requestMethod.equals("GET")) {
            if (ServiceFunctionPool.hasServiceFunctionOnPath(path)) {
                System.out.println("使用服务");
                ArrayList<ServiceArgument> args = new ArrayList<>();

                // TODO: 实现GET的动态参数提取

                return ServiceFunctionPool.runServiceFunction(path, args);
            }
            System.out.println("未找到服务");
        } else if (body != null) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            if (ServiceFunctionPool.hasServiceFunctionOnPath(path)) {
                System.out.println("使用服务");
                ArrayList<ServiceArgument> args = new ArrayList<>();
                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
                }
                return ServiceFunctionPool.runServiceFunction(path, args);
            }
            System.out.println("未找到服务");
        }
        System.out.println("未使用服务");
        return null;
    }
}

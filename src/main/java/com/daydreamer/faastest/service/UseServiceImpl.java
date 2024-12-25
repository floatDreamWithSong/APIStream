package com.daydreamer.faastest.service;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.controller.interfaces.UseService;
import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.service.common.ServiceModulePool;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class UseServiceImpl implements UseService {

    @Override
    public String useServiceFunction(HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
//        String userAgent = request.getHeader("User-Agent");
//        String ip = request.getRemoteAddr();
//        String query = request.getQueryString();
        String requestMethod = request.getMethod();
//        System.out.println("path: " + path);
//        System.out.println("userAgent: " + userAgent);
//        System.out.println("ip: " + ip);
//        System.out.println("query: " + query);
//        System.out.println("requestMethod: " + requestMethod);
//        System.out.println("body: ");
        if (requestMethod.equals("GET")) {
            if (ServiceModulePool.instance.hasServiceOnPath(path)) {
                log.info("使用服务");
                ArrayList<ServiceArgument> args = new ArrayList<>();

                // TODO: 实现GET的动态参数提取

                return ServiceModulePool.instance.callModule(path, args);
            }
        } else if (body != null) {
//            for (Map.Entry<String, Object> entry : body.entrySet()) {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
            if (ServiceModulePool.instance.hasServiceOnPath(path)) {
                ArrayList<ServiceArgument> args = new ArrayList<>();
                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
                }
                return ServiceModulePool.instance.callModule(path, args);
            }
        }
        log.info("未找到服务");
        return null;
    }
}

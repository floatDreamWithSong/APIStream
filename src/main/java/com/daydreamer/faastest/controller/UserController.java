package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.service.FunctionService;
import com.daydreamer.faastest.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    ManagerService managerService = new ManagerService();
    FunctionService functionService = new FunctionService();

    @RequestMapping(value = "/**")
    public Object handleRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Get方式，在路由后面传入的参数
        String query = request.getQueryString();
        String ip = request.getRemoteAddr();
        // 获取前端的data参数，就是post之类的传参
        Map<String, String[]> parameterMap = request.getParameterMap();
        String userAgent = request.getHeader("User-Agent");
        String requestMethod = request.getMethod();
        System.out.println(path + "," + query + "," + ip + "," + userAgent + "," + requestMethod);
        for(Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            System.out.println(key + "," + value[0]);
        }
//        if (Objects.equals(path, "/APIStreamFunctionService")){
//            switch (requestMethod) {
//                case "GET":
//                    return managerService.getService();
//                case "POST":
//                    return managerService.postService();
//                case "PUT":
//                    return managerService.putService();
//                case "DELETE":
//                    return managerService.deleteService();
//            }
//        } else if (functionService.isFunctionServicePathValid(path)){
//            return functionService.getFunctionService(path).runService();
//        }
        return new Default();
    }
}
class Default {
    public String message= "not matched";
}
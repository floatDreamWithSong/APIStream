package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.service.FunctionService;
import com.daydreamer.faastest.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    ManagerService managerService = new ManagerService();
    FunctionService functionService = new FunctionService();

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public Object handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return handleRequest(request, body);
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.DELETE)
    public Object handleDeleteRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return handleRequest(request, body);
    }
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.PUT)
    public Object handlePutRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return handleRequest(request, body);
    }
    /**
     *  由于Get方法没有Body，解析Body会报错，所以要单独拿出来，这里需要抽离公共逻辑，再做一层控制器
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public Object handleGetRequest(HttpServletRequest request) {
        return handleRequest(request, null);
    }

    private Object handleRequest(HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        // Get方式，在路由后面传入的参数
        String query = request.getQueryString();
        String requestMethod = request.getMethod();
        System.out.println(path + "," + query + "," + ip + "," + userAgent + "," + requestMethod);
        if(body != null)
        {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        if (Objects.equals(path, "/APIStreamFunctionService")){
            System.out.println("使用管理");
            switch (requestMethod) {
                case "GET":
                    return managerService.getService();
                case "POST":
                    return managerService.postService();
                case "PUT":
                    return managerService.putService();
                case "DELETE":
                    return managerService.deleteService();
            }
        } else
//            if (functionService.isFunctionServicePathValid(path))
            {
            System.out.println("使用服务");

            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            return functionService.useFunctionService(path, args);
        }
        return null;
    }
}
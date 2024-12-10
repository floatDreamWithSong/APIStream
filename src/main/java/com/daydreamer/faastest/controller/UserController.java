package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.dto.manage.*;
import com.daydreamer.faastest.service.FunctionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@RestController
public class UserController {

    AddFunctionService addFunctionService;
    DeleteFunctionService deleteFunctionService;
    QueryFunctionService queryFunctionService;
    UpdateFunctionService updateFunctionService;
    FunctionService functionService;

    @Autowired
    public UserController(AddFunctionService addFunctionService, DeleteFunctionService deleteFunctionService, QueryFunctionService queryFunctionService, UpdateFunctionService updateFunctionService, FunctionService functionService) {
        this.addFunctionService = addFunctionService;
        this.deleteFunctionService = deleteFunctionService;
        this.queryFunctionService = queryFunctionService;
        this.updateFunctionService = updateFunctionService;
        this.functionService = functionService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/APIStreamFunctionService")
    public AddFunctionServiceResponseEntity addServiceFunction(@RequestBody AddFunctionServiceJsonEntity body) {
        return addFunctionService.addFunction(body);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/APIStreamFunctionService")
    public DeleteFunctionServiceResponseEntity deleteServiceFunction(@RequestBody DeleteFunctionServiceJsonEntity body) {
        return deleteFunctionService.deleteFunctionService(body);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/APIStreamFunctionService")
    public UpdateFunctionServiceResponseEntity updateServiceFunction(@RequestBody UpdateFunctionServiceJsonEntity body) {
        return updateFunctionService.updateFunction(body);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamFunctionService/list")
    public QueryFunctionListResponseEntity queryFunctionList(@RequestParam QueryFunctionListParamEntity params) {
        return queryFunctionService.queryFunctionList(params);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/APIStreamFunctionService")
    public QueryFunctionDetailResponseEntity queryFunctionDetail(@RequestParam QueryFunctionDetailParamEntity params) {
        return queryFunctionService.queryFunctionDetail(params);
    }


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
     * 由于Get方法没有Body，解析Body会报错，所以要单独拿出来，这里需要抽离公共逻辑，再做一层控制器
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
        String query = request.getQueryString();
        String requestMethod = request.getMethod();
        System.out.println("path: " + path);
        System.out.println("userAgent: " + userAgent);
        System.out.println("ip: " + ip);
        System.out.println("query: " + query);
        System.out.println("requestMethod: " + requestMethod);
        System.out.println("body: ");
        if (body != null) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        if (functionService.isFunctionServicePathValid(path)) {
            System.out.println("使用服务");
            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            return functionService.useFunctionService(path, args);
        }
        System.out.println("未使用服务");
        return null;
    }
}
package com.daydreamer.faastest.controller;


import com.daydreamer.faastest.entity.dto.manage.*;
import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import com.daydreamer.faastest.service.UseServiceFunctionImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MainController {

    AddFunctionService addFunctionService;
    DeleteFunctionService deleteFunctionService;
    QueryFunctionService queryFunctionService;
    UpdateFunctionService updateFunctionService;
    UseServiceFunction functionService;

    @Autowired
    public MainController(AddFunctionService addFunctionService, DeleteFunctionService deleteFunctionService, QueryFunctionService queryFunctionService, UpdateFunctionService updateFunctionService, UseServiceFunction functionService) {
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
    public ServiceResult handlePostRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return functionService.useServiceFunction(request, body);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.DELETE)
    public ServiceResult handleDeleteRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return functionService.useServiceFunction(request, body);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.PUT)
    public ServiceResult handlePutRequest(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        return functionService.useServiceFunction(request, body);
    }

    /**
     * 由于Get方法没有Body，解析Body会报错，所以要单独拿出来，这里需要抽离公共逻辑，再做一层控制器
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public ServiceResult handleGetRequest(HttpServletRequest request) {
        return functionService.useServiceFunction(request, null);
    }

}
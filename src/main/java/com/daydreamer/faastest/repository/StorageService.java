package com.daydreamer.faastest.repository;

import com.daydreamer.faastest.common.modules.ServiceFunction;

import java.util.ArrayList;
import java.util.UUID;

public interface StorageService {
    /**
     * 储存API函数的名字，函数体，参数名及参数默认值
     * @param serviceFunction
     * @return
     */
    boolean storageServiceFunction(ServiceFunction serviceFunction);
    boolean deleteServiceFunction(UUID serviceFunctionId);
    ServiceFunction getServiceFunction(UUID serviceFunctionId);

    /**
     * 分页查询部署的部分云函数
     * @param start
     * @param pageSize
     * @return
     */
    ArrayList<ServiceFunction> getServiceFunctionByPaging(Integer start, Integer pageSize);
    Integer getServiceFunctionCount();
}

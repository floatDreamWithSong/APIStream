package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.QueryFunctionService;
import com.daydreamer.faastest.entity.dto.manage.receive.QueryFunctionDetailParamEntity;
import com.daydreamer.faastest.entity.dto.manage.response.QueryFunctionDetailResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.response.QueryFunctionListResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.receive.QueryFunctionListParamEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryFunctionServiceImpl implements QueryFunctionService {
    @Override
    public QueryFunctionListResponseEntity queryFunctionList(QueryFunctionListParamEntity params) {
        return null;
    }

    @Override
    public QueryFunctionDetailResponseEntity queryFunctionDetail(QueryFunctionDetailParamEntity params) {
        return null;
    }
}

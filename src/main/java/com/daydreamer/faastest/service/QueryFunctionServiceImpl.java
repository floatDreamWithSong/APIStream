package com.daydreamer.faastest.service;

import com.daydreamer.faastest.controller.QueryFunctionService;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionDetailResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionListResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionListParamEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryFunctionServiceImpl implements QueryFunctionService {
    @Override
    public QueryFunctionListResponseEntity queryFunctionList(QueryFunctionListParamEntity params) {
        return null;
    }

    @Override
    public QueryFunctionDetailResponseEntity queryFunctionDetail(QueryFunctionListParamEntity params) {
        return null;
    }
}

package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.receive.QueryFunctionDetailParamEntity;
import com.daydreamer.faastest.entity.dto.manage.response.QueryFunctionDetailResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.receive.QueryFunctionListParamEntity;
import com.daydreamer.faastest.entity.dto.manage.response.QueryFunctionListResponseEntity;

public interface QueryFunctionService {
    QueryFunctionListResponseEntity queryFunctionList(QueryFunctionListParamEntity params);
    QueryFunctionDetailResponseEntity queryFunctionDetail(QueryFunctionDetailParamEntity params);
}

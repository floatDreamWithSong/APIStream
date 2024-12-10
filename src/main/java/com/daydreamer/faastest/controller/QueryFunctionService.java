package com.daydreamer.faastest.controller;

import com.daydreamer.faastest.entity.dto.manage.QueryFunctionDetailParamEntity;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionDetailResponseEntity;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionListParamEntity;
import com.daydreamer.faastest.entity.dto.manage.QueryFunctionListResponseEntity;

public interface QueryFunctionService {
    QueryFunctionListResponseEntity queryFunctionList(QueryFunctionListParamEntity params);
    QueryFunctionDetailResponseEntity queryFunctionDetail(QueryFunctionDetailParamEntity params);
}

package com.daydreamer.apistream.common.context;

import com.daydreamer.apistream.common.dto.response.ServiceResult;

public interface JavascriptContext {

    ServiceResult callService(String evalStatement);

    void setService(Integer MaxConcurrent, String functionCode);
}

package com.daydreamer.apistream.common.dto.response;

import com.daydreamer.apistream.common.JsonProcessor;

public class ServiceResult {
    public String errorMessage;
    public String consoleOutput;
    public Object result;
    @Override
    public String toString() {
        return JsonProcessor.gson.toJson(this);
    }
}

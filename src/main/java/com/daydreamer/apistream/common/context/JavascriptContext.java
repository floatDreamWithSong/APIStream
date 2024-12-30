package com.daydreamer.apistream.common.context;

public interface JavascriptContext {

    String callService(String evalStatement);

    void setService(Integer MaxConcurrent, String functionCode);
}

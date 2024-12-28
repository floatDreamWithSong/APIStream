package com.daydreamer.faastest.common.dto.response;

import com.daydreamer.faastest.common.JsonProcessor;


public class UniResponse<T>{
    public Integer code;
    public String message;
    public T payload;
    public UniResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.payload = null;
    }
    public UniResponse(Integer code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }
    @Override
    public String toString() {
        return JsonProcessor.gson.toJson(this);
    }
}

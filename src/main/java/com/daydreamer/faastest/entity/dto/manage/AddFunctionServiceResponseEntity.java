package com.daydreamer.faastest.entity.dto.manage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AddFunctionServiceResponseEntity {
    public Integer code;
    public String message;
    public AddFunctionServiceResponseEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

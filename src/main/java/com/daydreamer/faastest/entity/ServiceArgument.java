package com.daydreamer.faastest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ServiceArgument {
    public String name;
    public Object value;
    public ServiceArgument(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}

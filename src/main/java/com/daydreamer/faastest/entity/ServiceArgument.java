package com.daydreamer.faastest.entity;

import lombok.Getter;

public class ServiceArgument{
    public final String name;
    public final Object value;
    public ServiceArgument(String name,Object value){
        this.name=name;
        this.value=value;
    }
}

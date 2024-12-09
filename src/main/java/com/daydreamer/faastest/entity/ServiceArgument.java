package com.daydreamer.faastest.entity;

import lombok.Getter;

public class ServiceArgument{
    public final String name;
    public final Object Value;
    public ServiceArgument(String name,Object Value){
        this.name=name;
        this.Value=Value;
    }
}

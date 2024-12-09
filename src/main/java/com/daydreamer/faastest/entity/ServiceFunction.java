package com.daydreamer.faastest.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ServiceFunction {
    private String serviceFunctionName;
    private  ArrayList<ServiceArgument> serviceFunctionArguments;
    private String ServiceCode;
    private UUID ServiceId;
    public ServiceFunction(String serviceFunctionName, ArrayList<ServiceArgument> serviceFunctionArguments, String ServiceCode) {
        this.serviceFunctionName = serviceFunctionName;
        this.serviceFunctionArguments = serviceFunctionArguments;
        this.ServiceCode = ServiceCode;
        ArrayList<ServiceArgument> serviceArguments = new ArrayList<>();
        this.ServiceId = UUID.randomUUID();
    }

    public Object runService() {
        return null;
    }
}


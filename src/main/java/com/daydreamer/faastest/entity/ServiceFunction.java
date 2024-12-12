package com.daydreamer.faastest.entity;

import com.daydreamer.faastest.context.JavascriptContext;
import com.daydreamer.faastest.context.JavascriptContextImpl;
import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import com.google.gson.Gson;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ServiceFunction {
    public String serviceFunctionName;
    public String serviceCode;
    public ArrayList<ServiceArgument> arguments;
    public UUID serviceId;
    public String CompleteCode;
    private JavascriptContext javascriptContext;

    public ServiceFunction(String serviceFunctionName, String ServiceCode, ArrayList<ServiceArgument> arguments) {
        this.serviceFunctionName = serviceFunctionName;
        this.serviceCode = ServiceCode;
        this.arguments = arguments;
        this.serviceId = UUID.randomUUID();
        StringBuilder completeCodeBuilder = new StringBuilder();
        completeCodeBuilder.append("function ").append(serviceFunctionName).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            completeCodeBuilder.append(arguments.get(i).name);
            if (i < arguments.size() - 1) {
                completeCodeBuilder.append(",");
            } else {
                completeCodeBuilder.append("){");
            }
        }
        completeCodeBuilder.append(ServiceCode).append("};");
        this.CompleteCode = completeCodeBuilder.toString();
        this.javascriptContext = new JavascriptContextImpl();
        this.javascriptContext.setServiceFunction(CompleteCode);
    }

    public ServiceResult runService(ArrayList<ServiceArgument> serviceFunctionArguments) {
        Gson gson = new Gson();
        StringBuilder jsCode = new StringBuilder();
        jsCode.append(serviceFunctionName).append("(");
        for (int i = 0; i < serviceFunctionArguments.size(); i++) {
            ServiceArgument serviceArgument = serviceFunctionArguments.get(i);
            String v = gson.toJson(serviceArgument.value);
            System.out.println(serviceArgument.value);
            System.out.println(v);
            jsCode.append("JSON.parse('");
            jsCode.append(v);
            if (i < serviceFunctionArguments.size() - 1) {
                jsCode.append("'),");
            } else {
                jsCode.append("'));");
            }
        }

        return javascriptContext.callServiceFunction(jsCode.toString());
    }

    public boolean resetServiceFunction(){
        return false;
    }
}


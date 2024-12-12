package com.daydreamer.faastest.entity;

import com.daydreamer.faastest.context.JavascriptContext;
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

    }

    public ServiceResult runService(ArrayList<ServiceArgument> serviceFunctionArguments) {
        ServiceResult serviceResult = new ServiceResult();
        JavascriptContext javascriptContext = new JavascriptContext();
        javascriptContext.context.eval("js", this.CompleteCode);
        ByteArrayOutputStream outputStream = javascriptContext.outputStream;
        Context context = javascriptContext.context;
        StringBuilder jsCode = new StringBuilder();
        Gson gson = new Gson();

        jsCode.append(serviceFunctionName).append("(");
        for (int i = 0; i < serviceFunctionArguments.size(); i++) {
            ServiceArgument serviceArgument = serviceFunctionArguments.get(i);
            Value v = context.asValue(gson.toJson(serviceArgument.value));
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
        System.out.println("执行代码:"+jsCode.toString());
        try {
            Value res = context.eval("js", jsCode.toString());
            System.out.println("执行结果:"+res);
            serviceResult.result = jsValue2JavaValue(res);
        } catch (Exception e) {
            System.out.println("执行错误:"+ e.getMessage());
            serviceResult.errorMessage = e.getMessage();
        }
        System.out.println("控制台输出:" + outputStream.toString());
        serviceResult.consoleOutput = outputStream.toString();
        outputStream.reset();
        return serviceResult;
    }

    Object jsValue2JavaValue(Value res) {
        if (res.isBoolean()) {
            return res.asBoolean();
        } else if (res.isNumber()) {
            return res.asDouble();
        } else if (res.isString()) {
            return res.asString();
        } else if (res.hasArrayElements()) {
            long size = res.getArraySize();
            ArrayList<Object> list = new ArrayList<>();
            for (long i = 0; i < size; i++) {
                Value element = res.getArrayElement(i);
                list.add(jsValue2JavaValue(element));
            }
            return list;
        } else if (res.hasMembers()) {
            return res.as(Object.class);
        }
        System.out.println("Exception" + res.hasArrayElements());
        return res.asString();
    }
}


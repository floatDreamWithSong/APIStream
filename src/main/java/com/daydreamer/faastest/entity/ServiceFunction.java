package com.daydreamer.faastest.entity;

import com.daydreamer.faastest.context.JavascriptContext;
import com.daydreamer.faastest.entity.dto.manage.ServiceResult;
import com.google.gson.Gson;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ServiceFunction {
    public String serviceFunctionName;
    public String serviceCode;
    public UUID serviceId;
    public ServiceFunction(String serviceFunctionName, String ServiceCode) {
        this.serviceFunctionName = serviceFunctionName;
        this.serviceCode = ServiceCode;
        ArrayList<ServiceArgument> serviceArguments = new ArrayList<>();
        this.serviceId = UUID.randomUUID();
    }

    public Object runService(ArrayList<ServiceArgument> serviceFunctionArguments) {
        ServiceResult serviceResult = new ServiceResult();
        ByteArrayOutputStream outputStream = JavascriptContext.outputStream;
        Context context = JavascriptContext.context;
            StringBuilder jsCode = new StringBuilder();
            jsCode.append("function ").append(serviceFunctionName).append("(");
            for (int i = 0; i < serviceFunctionArguments.size(); i++) {
                ServiceArgument serviceArgument = serviceFunctionArguments.get(i);
                jsCode.append(serviceArgument.name);
                if (i < serviceFunctionArguments.size() - 1) {
                    jsCode.append(", ");
                }else {
                    jsCode.append("){");
                }
            }
            jsCode.append(serviceCode).append("};");

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
                }else {
                    jsCode.append("'));");
                }
            }
            System.out.println(jsCode.toString());
            try{

                Value res = context.eval("js", jsCode.toString());
                System.out.println(res);
                serviceResult.result =  jsValue2JavaValue(res);
            }catch (Exception e){
                System.out.println(e.getMessage());
                serviceResult.errorMessage = e.getMessage();
            }
            System.out.println(outputStream.toString());
            serviceResult.consoleOutput = outputStream.toString();
            outputStream.reset();

        return serviceResult;
    }
    Object jsValue2JavaValue(Value res) {
        if(res.isBoolean()){
            return res.asBoolean();
        } else if (res.isNumber()) {
            return res.asDouble();
        } else if (res.isString()) {
            return res.asString();
        } else if (res.hasArrayElements()){
            long size = res.getArraySize();
            ArrayList<Object> list = new ArrayList<>();
            for (long i = 0; i < size; i++) {
                Value element = res.getArrayElement(i);
                list.add(jsValue2JavaValue(element));
            }
            return list;
        } else if (res.hasMembers()){
            return res.as(Object.class);
        }
        System.out.println("Exception"+res.hasArrayElements());
        return res.asString();
    }
}


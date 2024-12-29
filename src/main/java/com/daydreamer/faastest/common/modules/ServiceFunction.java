package com.daydreamer.faastest.common.modules;

import com.daydreamer.faastest.common.JsonProcessor;

import java.util.ArrayList;
import java.util.UUID;

public class ServiceFunction {
    public String serviceFunctionName;
    public String serviceCode;
    public ArrayList<ServiceArgument> arguments;

    public ServiceFunction(String serviceFunctionName, String ServiceCode, ArrayList<ServiceArgument> arguments) {
        this.serviceFunctionName = serviceFunctionName;
        this.arguments = arguments;
        this.serviceCode = ServiceCode;
    }

    public String getCallFunctionCode(ArrayList<ServiceArgument> serviceFunctionArguments) {

        StringBuilder jsCode = new StringBuilder();
        jsCode.append(serviceFunctionName).append("(");

        for (int i = 0; i < serviceFunctionArguments.size(); i++) {
            ServiceArgument serviceArgument = serviceFunctionArguments.get(i);
            String v = JsonProcessor.gson.toJson(serviceArgument.value);
            jsCode.append("JSON.parse('");
            jsCode.append(v);
            if (i < serviceFunctionArguments.size() - 1) {
                jsCode.append("'),");
            } else {
                jsCode.append("')");
            }
        }

        jsCode.append(");");
        return jsCode.toString();
    }
}


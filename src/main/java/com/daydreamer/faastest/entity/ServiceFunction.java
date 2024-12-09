package com.daydreamer.faastest.entity;

import com.daydreamer.faastest.entity.dto.manage.ServiceResult;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Context context = Context.newBuilder("js")
                .out(new PrintStream(outputStream))
                .build()) {
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
            jsCode.append(serviceFunctionName).append("(");
            for (int i = 0; i < serviceFunctionArguments.size(); i++) {
                ServiceArgument serviceArgument = serviceFunctionArguments.get(i);
                Value v = context.asValue(serviceArgument.value);
                jsCode.append(v);
                if (i < serviceFunctionArguments.size() - 1) {
                    jsCode.append(",");
                }else {
                    jsCode.append(");");
                }
            }
            System.out.println(jsCode.toString());
            try{
                Value res = context.eval(Source.create("js", jsCode.toString()));
                serviceResult.result = res;
            }catch (Exception e){
                System.out.println(e.getMessage());
                serviceResult.errorMessage = e.getMessage();
            }
            System.out.println(outputStream.toString());
            serviceResult.consoleOutput = outputStream.toString();
        }
        return serviceResult.result;
    }
}


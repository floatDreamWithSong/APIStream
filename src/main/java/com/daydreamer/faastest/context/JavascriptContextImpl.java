package com.daydreamer.faastest.context;

import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class JavascriptContextImpl implements JavascriptContext {
    public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public Context context = Context.newBuilder("js")
            .out(new PrintStream(outputStream))
            .allowAllAccess(true)
            .build();

    @Override
    public ServiceResult callServiceFunction(String evalStatement) {
        ServiceResult serviceResult = new ServiceResult();
        try {
            Value res = context.eval("js", evalStatement);
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

    @Override
    public boolean setServiceFunction(String functionCode) {
        this.context = Context.newBuilder("js")
                .out(new PrintStream(outputStream))
                .allowAllAccess(true)
                .build();
        this.context.eval("js", functionCode);
        return true;
    }

    private Object jsValue2JavaValue(Value res) {
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

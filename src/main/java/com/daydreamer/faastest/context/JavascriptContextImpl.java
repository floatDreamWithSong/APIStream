package com.daydreamer.faastest.context;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.*;

public class JavascriptContextImpl implements JavascriptContext {

    public Integer MaxConcurrent;
    private BlockingQueue<JavascriptContextCore> availableContext = new LinkedBlockingQueue<>();

    public JavascriptContextImpl(Integer MaxConcurrent, String functionCode) {
        setServiceFunction(MaxConcurrent, functionCode);
    }
    @Override
    public String callServiceFunction(String evalStatement) {
        ServiceResult serviceResult = new ServiceResult();
        JavascriptContextCore core = null;
        try {
            core = availableContext.take();
            Context context = core.context;
            ByteArrayOutputStream outputStream = core.outputStream;
            try {
                Value res = context.eval("js", evalStatement);
                serviceResult.result = jsValue2JavaValue(res);
                System.out.println("执行结果:"+res);
            } catch (Exception e) {
                serviceResult.errorMessage = e.getMessage();
                System.out.println("执行错误:"+ e.getMessage());
            }
            serviceResult.consoleOutput = outputStream.toString();
            System.out.println("控制台输出:" + outputStream.toString());
            outputStream.reset();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String resp = JsonProcessor.gson.toJson(serviceResult);
        if (core!=null)
            availableContext.offer(core);

        return resp;
    }

    @Override
    public void setServiceFunction (Integer MaxConcurrent, String functionCode) {
        System.out.println("MaxConcurrent: " + MaxConcurrent);
        this.availableContext = new LinkedBlockingQueue<>();
        this.MaxConcurrent = MaxConcurrent;
        for (int i = 0; i < MaxConcurrent; i++) {
            JavascriptContextCore core = new JavascriptContextCore();
            core.context.eval("js", functionCode);
            this.availableContext.add(core);
        }
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

class JavascriptContextCore {
    public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public Context context = Context.newBuilder("js")
            .out(new PrintStream(outputStream))
            .allowAllAccess(true)
            .build();
}
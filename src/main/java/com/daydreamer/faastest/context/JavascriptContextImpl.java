package com.daydreamer.faastest.context;

import com.daydreamer.faastest.entity.dto.service.ServiceResult;
import com.google.gson.Gson;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.*;

public class JavascriptContextImpl implements JavascriptContext {

    public Integer MaxConcurrent;
    private BlockingQueue<JavascriptContextCore> availableContext = new LinkedBlockingQueue<>();

    public JavascriptContextImpl(Integer MaxConcurrent, String functionCode) {
        System.out.println("MaxConcurrent: " + MaxConcurrent);
        this.MaxConcurrent = MaxConcurrent;
        for (int i = 0; i < MaxConcurrent; i++) {
//            contextList.add();
//            availableIndex.add(i);
            JavascriptContextCore core = new JavascriptContextCore();
            core.context.eval("js", functionCode);
            availableContext.add(core);
        }
    }
    @Override
    public String callServiceFunction(String evalStatement) {
        ServiceResult serviceResult = new ServiceResult();
        JavascriptContextCore core = null;
        try {
            System.out.println("尝试获取上下文资源");
            core = availableContext.take();
            System.out.println("获取到上下文资源"+core.uuid);
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
            System.out.println("获取上下文出现问题");
            e.printStackTrace();
        }
        String resp = new Gson().toJson(serviceResult);
        if (core!=null){
            System.out.println("释放上下文资源"+core.uuid);
            availableContext.offer(core);

        }
        return resp;
    }

    @Override
    public boolean setServiceFunction(String functionCode) {
//        this.context = Context.newBuilder("js")
//                .out(new PrintStream(outputStream))
//                .allowAllAccess(true)
//                .build();
//        this.context.eval("js", functionCode);
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

class JavascriptContextCore {
    public ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    public Context context = Context.newBuilder("js")
            .out(new PrintStream(outputStream))
            .allowAllAccess(true)
            .build();
    public UUID uuid = UUID.randomUUID();
}
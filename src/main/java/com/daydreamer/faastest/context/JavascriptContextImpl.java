package com.daydreamer.faastest.context;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.entity.dto.response.ServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.concurrent.*;

@Slf4j
public class JavascriptContextImpl implements JavascriptContext {

    public Integer MaxConcurrent;
    private BlockingQueue<JavascriptContextCore> availableContext = new LinkedBlockingQueue<>();

    public JavascriptContextImpl(Integer MaxConcurrent, String functionCode) {
        setServiceFunction(MaxConcurrent, functionCode);
    }
    @Override
    public String callServiceFunction(String evalStatement) {
        log.debug(evalStatement);
        ServiceResult serviceResult = new ServiceResult();
        JavascriptContextCore core = null;
        try {
            core = availableContext.take();
            Context context = core.context;
            ByteArrayOutputStream outputStream = core.outputStream;
            Value res = null;
            try {
                res = context.eval("js", evalStatement);
                serviceResult.result = jsValue2JavaValue(res);

            } catch (Exception e) {
                serviceResult.errorMessage = e.getMessage();
                log.error("执行错误:{}", e.getMessage());
            } finally {
                serviceResult.consoleOutput = outputStream.toString();
                log.info("执行结果: {} 控制台输出:{}", res, outputStream.toString());
                outputStream.reset();
            }
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
        log.debug("MaxConcurrent: {}", MaxConcurrent);
        log.debug("\nfunctionCode:\n {}", functionCode);
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
        log.error("Exception{}", res.hasArrayElements());
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
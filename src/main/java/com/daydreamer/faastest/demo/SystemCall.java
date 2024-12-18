package com.daydreamer.faastest.demo;

import com.daydreamer.faastest.common.JsonProcessor;

// SystemCall类
public class SystemCall {
    public String systemFunction() {
        // 执行系统调用或操作
        return JsonProcessor.gson.toJson(new SystemCallEntity());
    }
}
class SystemCallEntity {
    public String name= "name";
    public Integer age= 10;
}

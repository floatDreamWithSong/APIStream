package com.daydreamer.faastest.demo;
// Java代码
import org.graalvm.polyglot.*;

public class SystemCallDemo {
    public static void main(String[] args) {
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            // 评估JS代码
            context.eval("js", "print('Hello from JavaScript!');");
            // 调用Java方法
            context.getBindings("js").putMember("systemCall", new SystemCall());
            context.eval("js", "print(JSON.parse(systemCall.systemFunction()).age)");
        }
    }
}


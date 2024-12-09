package com.daydreamer.faastest.demo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class JavaScriptRunTimeTest {
    public static void main(String[] args) {
        // 创建一个字节数组输出流来捕获console.log输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Context context = Context.newBuilder("js")
                .out(new PrintStream(outputStream))
                .build()) {
            // 定义JavaScript代码，包含console.log语句
            String jsCode = "console.log('这是一个console.log消息');throw Error('what');";
            String jsFunction = "function add(a, b) { return a + b; }";
            context.eval("js", jsFunction);
            // 准备参数值
            int num1 = 3;
            int num2 = 5;
            // 调用JavaScript函数并获取返回值
            Value result = context.eval("js", "add(" + num1 + ", " + num2 + ")");
            // 将返回值转换为Java中的类型（这里假设返回值是数字，转换为int）
            int sum = result.asInt();
            System.out.println("两数之和为: " + sum);
            try{
                context.eval(Source.create("js", jsCode));
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            // 获取捕获的console.log输出并打印
            System.out.println(outputStream.toString());
        }
    }
}
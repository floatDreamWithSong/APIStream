package com.daydreamer.faastest.context;

import com.daydreamer.faastest.entity.dto.service.ServiceResult;

public interface JavascriptContext {
    /**
     * 队医单个服务函数，它是单例，单例应该拥有相同的上下文函数，而graalvm上下文不能多个线程访问，要保证可以同时运行多个上下文
     * 就需要提供一个统一的上下文的接口访问类，其实现类应该根据情况创建一定数量的幂等的context，并提供统一运行接口，需要实现多线程访问
     * @param evalStatement 每次执行的语句，已经保证是一条执行函数的语句
     * @return ServiceResult类的Json字符串
     */
    String callServiceFunction(String evalStatement);

    /**
     *  初始化或者重新设置所有上下文的执行函数，（不能仅修改原上下文，否则会导致已经被替换过的函数仍然是可访问的）
     * @param functionCode 保证是一条定义函数的语句
     * @return 是否设置成功
     */
    void setServiceFunction(Integer MaxConcurrent, String functionCode);
}

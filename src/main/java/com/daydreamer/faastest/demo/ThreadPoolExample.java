package com.daydreamer.faastest.demo;

import com.daydreamer.faastest.demo.MyTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

// ...

public class ThreadPoolExample {
    public static void main(String[] args) {
        // 创建一个固定大小为5的线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 提交10个任务到线程池
        for (int i = 1; i <= 10; i++) {
            executor.execute(new MyTask("Task-" + i));
        }

        // 关闭线程池，不接受新任务，但已提交的任务会继续执行
        executor.shutdown();

        // 等待所有任务完成，或者超时
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                // 超时后取消当前执行的任务
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            // 如果等待被中断，重新尝试关闭线程池
            executor.shutdownNow();
            // 保留中断状态
            Thread.currentThread().interrupt();
        }
    }
}
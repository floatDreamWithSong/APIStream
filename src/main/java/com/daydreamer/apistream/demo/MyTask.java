package com.daydreamer.apistream.demo;

public class MyTask implements Runnable {
    private final String taskName;

    public MyTask(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println(taskName + " is running on thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000); // 模拟任务执行时间
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(taskName + " was interrupted.");
        }
        System.out.println(taskName + " has finished.");
    }
}
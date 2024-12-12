package com.daydreamer.faastest.demo;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class ArrayListAccessExample {
//
//    private static final int TOTAL_THREADS = 10;
//    private static final int LIST_SIZE = 5;
//    private static final ArrayList<String> list = new ArrayList<>(LIST_SIZE);
//    private static final ConcurrentLinkedQueue<Integer> availableIndices = new ConcurrentLinkedQueue<>();
//
//    static {
//        for (int i = 0; i < LIST_SIZE; i++) {
//            list.add("Item " + (i + 1));
//            availableIndices.add(i); // 初始化时所有索引都是可用的
//        }
//    }
//
//    public static void main(String[] args) {
//        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREADS);
//
//        for (int i = 0; i < TOTAL_THREADS; i++) {
//            final int threadNumber = i;
//            executor.execute(() -> {
//                while (true) {
//                    Integer index = availableIndices.poll(); // 尝试获取一个可用的索引
//                    if (index != null) {
//                        try {
//                            // 模拟处理时间
//                            Thread.sleep((long) (Math.random() * 1000));
//                            // 访问ArrayList
//                            System.out.println("Thread " + threadNumber + "访问索引 " + index + " 的内容: " + list.get(index));
//                        } catch (InterruptedException e) {
//                            Thread.currentThread().interrupt();
//                        } finally {
//                            availableIndices.offer(index); // 访问完毕后将索引放回队列
//                        }
//                    }
//                }
//            });
//        }
//
//        // 这里我们不关闭线程池，因为线程会一直运行直到ArrayList被访问
//    }
//}
import java.util.ArrayList;
import java.util.concurrent.*;

public class ArrayListAccessExample {

    private static final int TOTAL_THREADS = 10;
    private static final int LIST_SIZE = 5;
    private static final ArrayList<String> list = new ArrayList<>(LIST_SIZE);
    private static final ConcurrentLinkedQueue<Integer> availableIndices = new ConcurrentLinkedQueue<>();

    static {
        for (int i = 0; i < LIST_SIZE; i++) {
            list.add("Item " + (i + 1));
            availableIndices.add(i); // 初始化时所有索引都是可用的
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_THREADS);
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < TOTAL_THREADS; i++) {
            final int threadNumber = i;
            completionService.submit(() -> {
                Integer index = availableIndices.poll(); // 尝试获取一个可用的索引
                if (index != null) {
                    try {
                        // 模拟处理时间
                        Thread.sleep((long) (Math.random() * 1000));
                        // 访问ArrayList
                        String result = "Thread " + threadNumber + "访问索引 " + index + " 的内容: " + list.get(index);
                        availableIndices.offer(index); // 访问完毕后将索引放回队列
                        return result;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "Thread " + threadNumber + " was interrupted";
                    }
                }
                return "No available index for thread " + threadNumber;
            });
        }

        // 获取并处理结果
        for (int i = 0; i < TOTAL_THREADS; i++) {
            Future<String> future = completionService.take(); // 阻塞直到任务完成
            String result = future.get(); // 获取任务结果
            System.out.println(result);
        }

        executor.shutdown(); // 关闭线程池
    }
}
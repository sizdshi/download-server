package com.example.manager;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Component
public class ConcurrentTaskExecutor {
    private volatile boolean canceled = false;
    private String url="https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    private String filePath="L:\\test\\";
    private int threadCount = 6;

    public volatile static int progress;

    public ConcurrentTaskExecutor() {
        this(null, null, 5);
    }

    public ConcurrentTaskExecutor(String url, String filePath, int threadCount) {
        this.url = url;
        this.filePath = filePath;
        this.threadCount = threadCount;
    }

    public void executeTask()  {
        int personCount = 10;
        //  beginLatch用于控制所有线程同时开始
        CountDownLatch beginLatch = new CountDownLatch(1);
        //  endLatch用于控制所有线程结束
        CountDownLatch endLatch = new CountDownLatch(personCount);
        //  exchanger用于交换数据
        Exchanger<Integer> exchanger = new Exchanger<Integer>();
        //  futureTaskList用于存储所有线程的FutureTask
        List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();


        for (int i = 0; i < personCount; i++) {
            futureTaskList.add(new FutureTask<String>(new ExecuteCallable(beginLatch, endLatch, exchanger, i, this)));
        }

        ExecutorService execService = Executors.newFixedThreadPool(threadCount);

        for (FutureTask<String> futureTask : futureTaskList) {
            execService.execute(futureTask);
        }

        new Thread(new InterruptRunnable(this, beginLatch)).start();

        beginLatch.countDown();

        Integer totalResult = Integer.valueOf(0);
        for (int i = 0; i < personCount; i++) {
            Integer partialResult = null;
            try {
                partialResult = exchanger.exchange(Integer.valueOf(0));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(partialResult != 0){
                totalResult = totalResult + partialResult;
                System.out.println(String.format("Progress: %s/%s", totalResult, personCount));
            }
        }

        try {
            endLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("--------------");
        for (FutureTask<String> futureTask : futureTaskList) {
            try {
                System.out.println(futureTask.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        execService.shutdown();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled){
        this.canceled = canceled;
    }

//    public static void main(String[] args)  {
//        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
//        executor.executeTask();
//    }
}

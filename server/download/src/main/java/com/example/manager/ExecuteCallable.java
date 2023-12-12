package com.example.manager;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class ExecuteCallable implements Callable<String> {
    private int id;
    private CountDownLatch beginLatch;
    private CountDownLatch endLatch;
    private Exchanger<Integer> exchanger;
    private ConcurrentTaskExecutor concurrentTaskExecutor;

    public ExecuteCallable(CountDownLatch beginLatch, CountDownLatch endLatch,
                           Exchanger<Integer> exchanger, int id,
                           ConcurrentTaskExecutor concurrentTaskExecutor) {
        this.beginLatch = beginLatch;
        this.endLatch = endLatch;
        this.exchanger = exchanger;
        this.id = id;
        this.concurrentTaskExecutor = concurrentTaskExecutor;
    }

    @Override
    public String call() throws Exception {
        beginLatch.await();
        if(concurrentTaskExecutor.isCanceled()){
            endLatch.countDown();
            exchanger.exchange(0);
            return String.format("Player :%s is given up", id);
        }
        long millis = (long) (Math.random() * 10 * 1000);
        String result = String.format("Player :%s arrived, use %s millis", id, millis);
        Thread.sleep(millis);
        System.out.println(result);
        exchanger.exchange(1);
        endLatch.countDown();
        return result;
    }


}

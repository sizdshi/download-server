package com.example.manager;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class InterruptRunnable implements Runnable {
    private ConcurrentTaskExecutor concurrentTaskExecutor;
    private CountDownLatch beginLatch;

    public InterruptRunnable(ConcurrentTaskExecutor concurrentTaskExecutor, CountDownLatch beginLatch) {
        this.concurrentTaskExecutor = concurrentTaskExecutor;
        this.beginLatch = beginLatch;
    }

    @Override
    public void run() {
        try {
            beginLatch.await();
            Thread.sleep(1000);
            concurrentTaskExecutor.setCanceled(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

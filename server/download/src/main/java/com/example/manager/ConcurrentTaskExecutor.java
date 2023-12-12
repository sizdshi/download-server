package com.example.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class ConcurrentTaskExecutor {
    private volatile boolean canceled = false;

    public void executeTask() throws Exception {
        int personCount = 10;
        int threadCount = 5;

        CountDownLatch beginLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(personCount);
        Exchanger<Integer> exchanger = new Exchanger<Integer>();

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
            Integer partialResult = exchanger.exchange(Integer.valueOf(0));
            if(partialResult != 0){
                totalResult = totalResult + partialResult;
                System.out.println(String.format("Progress: %s/%s", totalResult, personCount));
            }
        }

        endLatch.await();
        System.out.println("--------------");
        for (FutureTask<String> futureTask : futureTaskList) {
            System.out.println(futureTask.get());
        }
        execService.shutdown();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled){
        this.canceled = canceled;
    }

    public static void main(String[] args) throws Exception {
        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
        executor.executeTask();
    }
}

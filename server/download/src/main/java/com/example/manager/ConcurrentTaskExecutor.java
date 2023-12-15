package com.example.manager;

import com.example.service.HttpDownload;
import com.example.service.impl.HttpDownloadImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Component
@Slf4j
public class ConcurrentTaskExecutor {
    private volatile boolean canceled = false;
    private String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    private String filePath = "L:\\test\\";
    private int threadCount = 6;

    public volatile static int progress;

    public volatile static long len = 0;


    private static HttpDownload httpDownload;

    @Autowired
    public void setHttpDownload(HttpDownload httpDownload) {
        ConcurrentTaskExecutor.httpDownload = httpDownload;
    }

    public ConcurrentTaskExecutor() {
        this(null, null, 5);
    }

    public ConcurrentTaskExecutor(String url, String filePath, int threadCount) {
        this.url = url;
        this.filePath = filePath;
        this.threadCount = threadCount;
    }

    public void executeTask() {
        try {
            int actualThread = 0;

            log.info("httpDownload class: {}", httpDownload.getClass().getName());
            httpDownload.setUrl(url);

            long totalFileSize = httpDownload.getFileSize();
            log.info("totalFile: {}", totalFileSize);
            ConcurrentTaskExecutor.len = totalFileSize;

            long chunkSize = calculateChunkSize(totalFileSize);
            log.info("chunkSize: {}", chunkSize);

            String fileName = url.substring(url.lastIndexOf('/') + 1);

            RandomAccessFile file = new RandomAccessFile(filePath + fileName, "rw");
            file.setLength(totalFileSize);
            //  completedChunks用于存储已经下载完成的分片
            Set<Long> completedChunks = loadCompletedChunks();

            int totalTasks = (int) Math.ceil((double) totalFileSize / chunkSize);
            //  beginLatch用于控制所有线程同时开始
            CountDownLatch beginLatch = new CountDownLatch(1);
            //  endLatch用于控制所有线程结束
            CountDownLatch endLatch = new CountDownLatch(totalTasks);
            //  exchanger用于交换数据
            Exchanger<Integer> exchanger = new Exchanger<Integer>();
            //  futureTaskList用于存储所有线程的FutureTask
            List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();


            for (int i = 0; i < totalTasks; i++) {
                long startIndex = i * chunkSize;
                long endIndex = Math.min(startIndex + chunkSize - 1, totalFileSize - 1);
                System.out.println("任务：" + i + "下载" + startIndex + "--->" + endIndex);
                if (completedChunks.contains(Long.valueOf(i))) {
                    //  如果该分片已经下载完成，则跳过
                    actualThread--;
                    System.out.println("任务：" + i + "已下载，跳过");
                    continue;
                }
                actualThread++;
                futureTaskList.add(new FutureTask<>(new ExecuteCallable(beginLatch, endLatch, exchanger, i, this, startIndex, endIndex)));
//                futureTaskList.add(new FutureTask<>(new ExecuteCallable(endLatch, exchanger, i, this, startIndex, endIndex)));

            }

            ExecutorService execService = Executors.newFixedThreadPool(threadCount);

            for (FutureTask<String> futureTask : futureTaskList) {
                execService.submit(futureTask);
            }

//        new Thread(new InterruptRunnable(this, beginLatch)).start();


            beginLatch.countDown();


            Integer totalResult = Integer.valueOf(0);
//            for (int i = 0; i < actualThread; i++) {
//                Integer partialResult = null;
//                try {
//                    partialResult = exchanger.exchange(Integer.valueOf(0));
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                if (partialResult != 0) {
//                    totalResult = totalResult + partialResult;
//                    System.out.println(String.format("Progress: %s/%s", totalResult, actualThread));
//                }
//            }

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    private long calculateChunkSize(long totalFileSize) {
        if (totalFileSize <= 32 * 1024) {
            return 32 * 1024;  // 最小32KB
        } else if (totalFileSize <= 10 * 1024 * 1024) {
            return 1 * 1024 * 1024;  // 1MB
        } else if (totalFileSize <= 100 * 1024 * 1024) {
            return 10 * 1024 * 1024;  // 10MB
        } else {
            // 超过100MB的按照10MB进行分片
            return 10 * 1024 * 1024;
        }
    }

    private Set<Long> loadCompletedChunks() {
        Set<Long> completedChunks = new HashSet<>();
        try {
            File scoreboardFile = new File(filePath, "scoreboard.txt");
            if (scoreboardFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(scoreboardFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        long chunkIndex = Long.parseLong(line.trim());
                        completedChunks.add(chunkIndex);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return completedChunks;
    }
}

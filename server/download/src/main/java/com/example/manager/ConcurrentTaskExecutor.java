package com.example.manager;

import com.example.service.HttpDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Slf4j
@Component
@Scope("prototype")
public class ConcurrentTaskExecutor {
    private volatile boolean canceled = false;
    public volatile static int progress;

    private String urlPath ;

    private String savePath ;

    private int threadCount = 6;
    public volatile static long len = 0;

    private ThreadPoolTaskExecutor taskExecutor;

    private HttpDownload httpDownload;

    @Autowired
    public void setHttpDownload(HttpDownload httpDownload) {
        this.httpDownload = httpDownload;
    }

    public ConcurrentTaskExecutor() {
        this(null, null, 5);
    }

    public ConcurrentTaskExecutor(String urlPath, String savePath,int threadCount) {
        this.urlPath = urlPath;
        this.savePath = savePath;
        this.threadCount = threadCount;
        this.taskExecutor = createThreadPoolTaskExecutor();
    }

    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadCount);
        executor.setMaxPoolSize(threadCount);
//        String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
        executor.setThreadNamePrefix("_download--");
        executor.initialize();
        return executor;
    }

    public void setThreadCount(int newThreadCount) {
        this.threadCount = newThreadCount;
        // 如果你希望在运行时修改线程池的大小，可以在这里进行设置
        taskExecutor.setCorePoolSize(newThreadCount);
        taskExecutor.setMaxPoolSize(newThreadCount);
    }
    
    @Async
    public void executeTask() throws Exception {
        int personCount = 10;
        httpDownload.setUrl(urlPath);
        int actualThread = 0;
        long totalFileSize = httpDownload.getFileSize();
        log.info("totalFile: {}", totalFileSize);
        ConcurrentTaskExecutor.len = totalFileSize;
        long chunkSize = calculateChunkSize(totalFileSize);
        log.info("chunkSize: {}", chunkSize);
        String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);

//        RandomAccessFile file = new RandomAccessFile(savePath + fileName, "rwd");
//        file.setLength(totalFileSize);
        //  completedChunks用于存储已经下载完成的分片
        Set<Long> completedChunks = loadCompletedChunks();

        int totalTasks = (int) Math.ceil((double) totalFileSize / chunkSize);
        CountDownLatch beginLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(totalTasks);
        Exchanger<Integer> exchanger = new Exchanger<Integer>();

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
            futureTaskList.add(new FutureTask<String>(new ExecuteCallable(beginLatch, endLatch, exchanger, i, this,startIndex,endIndex,urlPath,savePath)));
//            futureTaskList.add(new FutureTask<String>(new ExecuteCallable( endLatch, exchanger, i, this,startIndex,endIndex,urlPath,savePath)));

        }


        ExecutorService execService = Executors.newFixedThreadPool(threadCount);
        for (FutureTask<String> futureTask : futureTaskList) {
            taskExecutor.execute(futureTask);
            System.out.println("提交任务"+futureTask.toString());
        }

//        List<Future<String>> futures = taskExecutor.getThreadPoolExecutor().invokeAll(futureTaskList);

//        new Thread(new InterruptRunnable(this, beginLatch)).start();

        beginLatch.countDown();

        Integer totalResult = Integer.valueOf(0);
        for (int i = 0; i < actualThread; i++) {
            Integer partialResult = exchanger.exchange(Integer.valueOf(0));
            if(partialResult != 0){
                totalResult = totalResult + partialResult;
                System.out.println(String.format("Progress: %s/%s", totalResult, actualThread));
            }
        }

        endLatch.await();
        System.out.println("--------------");
        for (FutureTask<String> futureTask : futureTaskList) {
            System.out.println(futureTask.get());
        }
      taskExecutor.shutdown();
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled){
        this.canceled = canceled;
    }



    private Set<Long> loadCompletedChunks() {
        Set<Long> completedChunks = new HashSet<>();
        try {
            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
            File scoreboardFile = new File(savePath, fileName+"_scoreboard.txt");
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




}

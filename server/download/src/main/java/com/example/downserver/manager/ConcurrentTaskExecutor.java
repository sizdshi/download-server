package com.example.downserver.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.download.common.ErrorCode;
import com.example.downserver.model.entity.Download;
import com.example.downserver.service.DownloadService;
import com.example.exception.BusinessException;
import com.example.downserver.service.SpeedListener;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

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
@Service
public class ConcurrentTaskExecutor  {
    private volatile boolean canceled = false;
    public volatile long progress;
    private volatile boolean downloadInProgress = true;

    @Setter

    private  String urlPath ;

    @Setter
    private  String savePath ;

    private  int threadCount = 6;


    private ThreadPoolTaskExecutor taskExecutor;


    private long totalFileSize;

    private int totalTasks;

    private final Object lock = new Object();

    SpeedListener speedListener = null;

    @Autowired
    private DownloadService downloadService;


    public long getProgress(){
        return progress;
    }

    public String getUrlPath(){
        return urlPath;
    }

    public String getSavePath(){
        return savePath;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public long getFileSize(){
        return totalFileSize;
    }

    public void updateProgress(long delta){
        progress+=delta;
    }




    private List<SpeedListener> speedListeners = new ArrayList<>();

    public void addSpeedListener(SpeedListener speedListener) {
//        speedListeners.add(speedListener);
        this.speedListener = speedListener;
    }

    public void removeSpeedListener(SpeedListener speedListener) {
        speedListeners.remove(speedListener);
    }

    // 通知监听器速度和进度的方法
    private void notifySpeedUpdate(int speed, double progress) {
        for (SpeedListener listener : speedListeners) {
            listener.speed(speed, progress);
        }
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
        executor.setThreadNamePrefix("_download--");
        executor.initialize();
        return executor;
    }

    public void setThreadCount(int newThreadCount) {
        this.threadCount = newThreadCount;
        taskExecutor.shutdown(); // 关闭现有的执行器
        taskExecutor = createThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(newThreadCount);
//        taskExecutor.setMaxPoolSize(newThreadCount);
    }


    public void executeTask() throws Exception {
        int personCount = 10;

        int actualThread = 0;
        totalFileSize = getTotalFileSize(urlPath);
        log.info("totalFile: {}", totalFileSize);
//        ConcurrentTaskExecutor.len = totalFileSize;
        long chunkSize = calculateChunkSize(totalFileSize);
        log.info("chunkSize: {}", chunkSize);
        String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);

//        RandomAccessFile file = new RandomAccessFile(savePath + fileName, "rwd");
//        file.setLength(totalFileSize);
        //  completedChunks用于存储已经下载完成的分片
        Set<Long> completedChunks = loadCompletedChunks();

        totalTasks = (int) Math.ceil((double) totalFileSize / chunkSize);
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
            taskExecutor.submit(futureTask);
            System.out.println("提交任务"+futureTask.toString());
        }

//        List<Future<String>> futures = taskExecutor.getThreadPoolExecutor().invokeAll(futureTaskList);

        new Thread(new InterruptRunnable(this, beginLatch,speedListener)).start();


        beginLatch.countDown();


//        Integer totalResult = Integer.valueOf(0);
//        for (int i = 0; i < actualThread; i++) {
//            Integer partialResult = exchanger.exchange(Integer.valueOf(0));
//            if(partialResult != 0){
//                totalResult = totalResult + partialResult;
//                System.out.println(String.format("Progress: %s/%s", totalResult, actualThread));
//            }
//        }

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

    private long getTotalFileSize(String fileUrl) {
        URL url = null;
        try {
            url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //发送一个普通的GET请求，只获取文件大小而不读取内容
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");

            long totalFileSize = connection.getContentLengthLong();
//        MultipleThreadDownloadManager.len=totalFileSize;
            connection.disconnect();
            return totalFileSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void speed(){
        long currentProgress = 0;
        while(downloadInProgress){
            currentProgress = progress;
            Set<Long> completedChunks = loadCompletedChunks();
            int downloadedChunks = completedChunks.size();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //下载百分比
            double downloadedPercentage = (double)  downloadedChunks /  (double) totalTasks* 100;
            // 当前下载进度除以文件总长得到下载进度
            double percent =  currentProgress / (double)totalFileSize * (100 - downloadedPercentage) + downloadedPercentage;
            // 当前下载进度减去前一秒的下载进度就得到一秒内的下载速度
            currentProgress=progress-currentProgress;
            speedListener.speed(currentProgress,percent);
            // 判断是否所有分片下载完成
            boolean allChunksDownloaded = checkAllChunksDownloaded();
            if (allChunksDownloaded) {
                downloadInProgress = false;
                currentProgress = totalFileSize-progress;
                speedListener.speed(currentProgress, 100);
                System.out.println("文件下载完毕");
            }

        }

    }

    private boolean checkAllChunksDownloaded() {
        Set<Long> completedChunks = loadCompletedChunks();

//        long totalTasks = (long) Math.ceil((double) len / concurrentTaskExecutor.getTotalTasks());

        for (long i = 0; i < totalTasks; i++) {
            if (!completedChunks.contains(i)) {
                return false;
            }
        }

        return true;
    }


//    @Scheduled(fixedRate = 200)
//    private void startDatabasePollingTask() {
//        log.info("轮训数据库");
//        boolean canceled = checkDatabaseStatus();
//        if (canceled) {
//            setCanceled(true);
//            System.out.println("取消下载");
//        }
//
//    }


    private boolean checkDatabaseStatus(){
        // 检查数据库是否有取消下载的请求
        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Download::getUrl, getUrlPath());
        lambdaQueryWrapper.eq(Download::getIs_delete,0);
        Download download = downloadService.getOne(lambdaQueryWrapper);

        if (download == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未获取到Download对象");
        }

        String taskStatus = download.getStatus();
        System.out.println("当前任务状态： "+taskStatus);
        switch (taskStatus) {
            case "deleted":
            case "pending":
                return true;
            case "downloading":
            case "completes":
            default:
                return false;
        }

    }
}

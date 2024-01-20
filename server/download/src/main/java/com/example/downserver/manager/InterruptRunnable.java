package com.example.downserver.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.download.common.ErrorCode;
import com.example.downserver.model.entity.Download;
import com.example.downserver.service.DownloadService;
import com.example.exception.BusinessException;
import com.example.service.SpeedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@Component
@Slf4j
public class InterruptRunnable implements Runnable {
    private ConcurrentTaskExecutor concurrentTaskExecutor;
    private CountDownLatch beginLatch;

    private volatile boolean downloadInProgress = true;


    private SpeedListener speedListener;


    @Autowired
    private DownloadService downloadService;


    public InterruptRunnable() {
        this(null, null, null);
    }

    public InterruptRunnable(ConcurrentTaskExecutor concurrentTaskExecutor, CountDownLatch beginLatch
            , SpeedListener speedListener) {
        this.concurrentTaskExecutor = concurrentTaskExecutor;
        this.beginLatch = beginLatch;
        this.speedListener = speedListener;
    }

    @Override
    public void run() {
        try {
            beginLatch.await();
//            startDatabasePollingTask();
            startSpeedCheckTask();

//            concurrentTaskExecutor.setCanceled(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Set<Long> loadCompletedChunks() {
        Set<Long> completedChunks = new HashSet<>();
        String fileName = concurrentTaskExecutor.getUrlPath().substring(concurrentTaskExecutor.getUrlPath().lastIndexOf('/') + 1);
        try {
            File scoreboardFile = new File(concurrentTaskExecutor.getSavePath(), fileName+"_scoreboard.txt");
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

    private boolean checkAllChunksDownloaded() {
        Set<Long> completedChunks = loadCompletedChunks();

//        long totalTasks = (long) Math.ceil((double) len / concurrentTaskExecutor.getTotalTasks());

        for (int i = 0; i < concurrentTaskExecutor.getTotalTasks(); i++) {
            if (!completedChunks.contains((long) i)) {
                return false;
            }
        }

        return true;
    }

    private void startSpeedCheckTask() {
        long currentProgress = 0;
        while (downloadInProgress && !concurrentTaskExecutor.isCanceled()) {
            currentProgress = concurrentTaskExecutor.getProgress();
            Set<Long> completedChunks = loadCompletedChunks();
            int downloadedChunks = completedChunks.size();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //下载百分比
            double downloadedPercentage = (double) downloadedChunks / (double) concurrentTaskExecutor.getTotalTasks() * 100;
            // 当前下载进度除以文件总长得到下载进度
            double progress = currentProgress / (double) concurrentTaskExecutor.getFileSize() * (100 - downloadedPercentage) + downloadedPercentage;
            // 当前下载进度减去前一秒的下载进度就得到一秒内的下载速度
            currentProgress = concurrentTaskExecutor.getProgress() - currentProgress;
            speedListener.speed(currentProgress, progress);
            // 判断是否所有分片下载完成
            boolean allChunksDownloaded = checkAllChunksDownloaded();
            if (allChunksDownloaded) {
                downloadInProgress = false;
                currentProgress = concurrentTaskExecutor.getFileSize() - concurrentTaskExecutor.getProgress();
                speedListener.speed(currentProgress, 100);
                System.out.println("文件下载完毕");
            }
        }
    }

//    @Scheduled(fixedRate = 200)
//    private void startDatabasePollingTask(){
//        log.info("轮训数据库");
//
//        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper();
//        lambdaQueryWrapper.eq(Download::getUrl, concurrentTaskExecutor.getUrlPath());
//        lambdaQueryWrapper.eq(Download::getIs_delete,0);
//        Download download = downloadService.getOne(lambdaQueryWrapper);
//
//        if (download == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未获取到Download对象");
//        }
//
//        String taskStatus = download.getStatus();
//        System.out.println("当前任务状态： "+taskStatus);
////        boolean canceled = checkDatabaseStatus();
////        if(canceled){
////            concurrentTaskExecutor.setCanceled(true);
////            System.out.println("取消下载");
////        }
//
//    }

    private boolean checkDatabaseStatus(){
        // 检查数据库是否有取消下载的请求

        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Download::getUrl, concurrentTaskExecutor.getUrlPath());
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

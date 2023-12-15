package com.example.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.ErrorCode;
import com.example.exception.BusinessException;
import com.example.model.entity.Download;
import com.example.service.DownloadService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Service
public class InterruptRunnable implements Runnable {
    private ConcurrentTaskExecutor concurrentTaskExecutor;
    private CountDownLatch beginLatch;

    private  volatile boolean paused = false;

    String urlPath = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    @Resource
    private DownloadService downloadService;

    public InterruptRunnable() {
        this(null, null);
    }

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

//    @Scheduled(fixedDelay = 200)
    private void pollDataBase() {
        String status = getDataBaseStatus();
        if (status == null || status.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未获取到Download对象");
        }
        handleDataBaseStatus(status);
        System.out.println("轮询数据库" + status);
    }


    private String getDataBaseStatus() {
        // 实现从数据库中获取任务状态的逻辑
        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Download::getUrl, urlPath);
        lambdaQueryWrapper.eq(Download::getIs_delete,0);
        Download download = downloadService.getOne(lambdaQueryWrapper);
        if (download == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未获取到Download对象");
        }
        // 返回任务状态，例如 DownloadStatus.STATUS_PAUSED
        return download.getStatus();
    }

    private void handleDataBaseStatus(String databaseStatus) {
        try {
            switch (databaseStatus) {
                case "pending":
                    paused = true;
                    break;
                case "downloaded":
                case "canceled":
                    paused = false;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

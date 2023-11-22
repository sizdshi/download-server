package com.example.manager;

import com.example.common.DownLoader;
import com.example.service.SpeedListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class MultipleThreadDownloadManager extends DownLoader {
    //线程下载数量
    public static int threadCount = 4;
    //记录子线程数量
    public static int runningThread = 1;
    //文件总大小
    public volatile static long len = 0;
    //文件进度
    public volatile static int progress;

    private volatile boolean downloadInProgress = true;



    public MultipleThreadDownloadManager(@Value("") String urlPath, @Value("") String savePath) {
        super(urlPath, savePath);
        createScoreboardFile();

    }

    public void setThreadCount(int threadCount) {
        MultipleThreadDownloadManager.threadCount = threadCount;
        runningThread = threadCount;
    }


    @Override
    public void run() {
        try {

            long totalFileSize = getTotalFileSize(getUrlPath());

            MultipleThreadDownloadManager.len = totalFileSize;
            long chunkSize = calculateChunkSize(totalFileSize);

            String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);
            RandomAccessFile file = new RandomAccessFile(getTempPath()+fileName, "rw");
            file.setLength(totalFileSize);

            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

            //读取任务完成记分板文件
            Set<Long> completedChunks = loadCompletedChunks();

            long totalTasks = (long) Math.ceil((double) totalFileSize /chunkSize);

            for (int i = 0; i < totalTasks; i++) {
                //下载的开始位置
                long startIndex = i * chunkSize;
                //结束位置
                long endIndex = Math.min((i+1)*chunkSize-1, totalFileSize - 1);
                System.out.println("任务：" + i + "下载" + startIndex + "--->" + endIndex);
                // 新增：如果分片已下载，则跳过
                if (completedChunks.contains((long)i)) {
                    System.out.println("任务：" + i + "已下载，跳过");
                    continue;
                }
                SubThreadDownload sonThreadDownload = new SubThreadDownload(getUrlPath(), getSavePath());
                sonThreadDownload.setter(i, startIndex, endIndex);
                sonThreadDownload.setChunkIndex(i);
                executorService.submit(sonThreadDownload);
            }
            executorService.shutdown();

            //开始监听下载进度
            speed();
            // 启动一个单独的监控线程，用于实时更新下载进度
//            new Thread(this::monitorProgress).start();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void speed() {
        long totalTasks = (long) Math.ceil((double) len / calculateChunkSize(len));

        int temp = 0;
        //循环监控网速，如果下载进度达到100%就结束监控
        while (downloadInProgress) {
            temp = progress;
            Set<Long> completedChunks = loadCompletedChunks();
            int downloadedChunks = completedChunks.size();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double downloadedPercentage =(double) downloadedChunks / (double) totalTasks  * 100;

            //当前下载进度除以文件总长得到下载进度
            double p = (double) temp / (double) len * (100-downloadedPercentage) +downloadedPercentage;
            //当前下载进度减去前一秒的下载进度就得到一秒内的下载速度
            temp = progress - temp;

            speedListener.speed(temp, p);
            // 判断是否所有分片下载完成
            boolean allChunksDownloaded = checkAllChunksDownloaded();
            if (allChunksDownloaded) {
                downloadInProgress=false;
                speedListener.speed(progress, 100);
                System.out.println("文件下载完毕");
            }
        }

    }

    SpeedListener speedListener = null;

    /**
     * @param speedListener 网速监听回调接口
     */
    public void addSpeedListener(SpeedListener speedListener) {
        this.speedListener = speedListener;
    }


    private long getTotalFileSize(String fileUrl){
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

    private long calculateChunkSize(long totalFileSize){
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

    private Set<Long> loadCompletedChunks(){
        Set<Long> completedChunks = new HashSet<>();
        try {
            File scoreboardFile = new File(getSavePath(), "scoreboard.txt");
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

    private void createScoreboardFile(){
        try {
            File scoreboardFile = new File(getSavePath(),"scoreboard.txt");
            if (!scoreboardFile.exists()) {
                scoreboardFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建计分板文件失败");
        }
    }


    // 监控下载进度的方法
    private  void monitorProgress() {
        try {
            long totalTasks = (long) Math.ceil((double) len / calculateChunkSize(len));
            while (downloadInProgress) {
                Set<Long> completedChunks = loadCompletedChunks();
                int downloadedChunks = completedChunks.size();
                int temp = progress;
                Thread.sleep(1000);
                // 计算已下载的百分比
                double downloadedPercentage = (double) downloadedChunks / (double) totalTasks * 100;
                System.out.println("已下载进度"+downloadedPercentage);
                // 当前下载进度除以文件总长得到下载进度
                double p = (double) temp / (double) len * (100-downloadedPercentage)+downloadedPercentage;
                // 当前下载进度减去前一秒的下载进度就得到一秒内的下载速度
                temp = progress - temp;
                speedListener.speed(temp, p);
                // 判断是否所有分片下载完成
                boolean allChunksDownloaded = checkAllChunksDownloaded();
                if (allChunksDownloaded) {
                    downloadInProgress=false;
                    speedListener.speed(progress, 100);
                    System.out.println("文件下载完毕");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private boolean checkAllChunksDownloaded() {
        Set<Long> completedChunks = loadCompletedChunks();

        long totalTasks = (long) Math.ceil((double) len / calculateChunkSize(len));

        for (long i = 0; i < totalTasks; i++) {
            if (!completedChunks.contains(i)) {
                return false;
            }
        }

        return true;
    }


}

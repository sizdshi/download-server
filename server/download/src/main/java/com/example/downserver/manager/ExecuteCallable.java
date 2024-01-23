package com.example.downserver.manager;

import com.example.common.DownLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Slf4j
@Service
public class ExecuteCallable extends DownLoader {
    private int threadId;
    private CountDownLatch beginLatch;
    private CountDownLatch endLatch;
    private Exchanger<Integer> exchanger;
    private ConcurrentTaskExecutor concurrentTaskExecutor;

    private long startIndex;
    private long endIndex;

//    private static HttpDownload httpDownload;
//
//    @Autowired
//    public void setHttpDownload(HttpDownload httpDownload) {
//        ExecuteCallable.httpDownload = httpDownload;
//    }

    public ExecuteCallable() {
        super(null,null);
        // 无参构造函数的内容
    }

    public ExecuteCallable(CountDownLatch beginLatch, CountDownLatch endLatch,
                           Exchanger<Integer> exchanger, int id,
                           ConcurrentTaskExecutor concurrentTaskExecutor) {
        super(null, null);
        this.beginLatch = beginLatch;
        this.endLatch = endLatch;
        this.exchanger = exchanger;
        this.threadId = id;
        this.concurrentTaskExecutor = concurrentTaskExecutor;
    }

    public ExecuteCallable(CountDownLatch beginLatch, CountDownLatch endLatch,
                           Exchanger<Integer> exchanger, int id,
                           ConcurrentTaskExecutor concurrentTaskExecutor, long startIndex,
                           long endIndex, String urlPath, String savePath) {
        super(urlPath, savePath);
        this.beginLatch = beginLatch;
        this.endLatch = endLatch;
        this.exchanger = exchanger;
        this.threadId = id;
        this.concurrentTaskExecutor = concurrentTaskExecutor;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public ExecuteCallable(CountDownLatch endLatch, Exchanger<Integer> exchanger, int id, ConcurrentTaskExecutor concurrentTaskExecutor, long startIndex, long endIndex, String urlPath, String savePath) {
        super(urlPath, savePath);

        this.endLatch = endLatch;
        this.exchanger = exchanger;
        this.threadId = id;
        this.concurrentTaskExecutor = concurrentTaskExecutor;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public ExecuteCallable(String urlPath, String savePath) {
        super(urlPath, savePath);
    }

    @Override
    public String call() throws Exception {
        beginLatch.await();
        System.out.println("开始下载"+threadId);

        if (concurrentTaskExecutor.isCanceled()) {

            exchanger.exchange(0);
            System.out.println("线程 "+threadId+"号：  停止");
            endLatch.countDown();
            return String.format("线程 :%s 停止", threadId);
        }

        InputStream inputStream = null;
        RandomAccessFile file = null;

        //定位文件
        try {
            URL url = new URL(getUrlPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                file = new RandomAccessFile(getSavePath() + fileName, "rwd");
                inputStream = connection.getInputStream();

                synchronized (file) {
                    file.seek(startIndex);
                    int len = 0;
                    byte[] buff = new byte[1024 * 1024];
                    //已经下载的数据长度
                    int total = 0;
                    while ((len = inputStream.read(buff)) != -1) {
                        file.write(buff, 0, len);
                        concurrentTaskExecutor.updateProgress(len);
                    }
                    // 下载完成后，写入任务记分板文件
                    writeCompletedChunkToScoreboard(threadId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(threadId + "号线程下载错误");
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    System.out.println("关闭文件失败");
                }
            }
        }


//        System.out.println("线程：" + threadId + "号下载完毕了");

        long millis = (long) (Math.random() * 10 * 1000);
        String result = String.format("线程 :%s 已完成下载, 耗时 %s millis", threadId, millis);

//        System.out.println(result);
//        exchanger.exchange(1);
        endLatch.countDown();


        return result;
    }

    private void writeCompletedChunkToScoreboard(long chunkIndex) {
        String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);
        File scoreboardFile = new File(getSavePath(), fileName+"_scoreboard.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreboardFile, true))) {
            writer.write(Long.toString(chunkIndex));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入任务记分板文件失败");
        }
    }

}

package com.example.manager;

import com.example.common.DownLoader;
import com.example.service.HttpDownload;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
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
public class ExecuteCallable extends DownLoader {
    /**
     * 线程id
     */
    private int threadId;
    /**
     * 用于控制所有线程同时开始
     */
    private CountDownLatch beginLatch;
    /**
     * 用于控制所有线程结束
     */
    private CountDownLatch endLatch;
    /**
     * 用于交换数据
     */
    private Exchanger<Integer> exchanger;
    /**
     * 用于控制线程池
     */
    private ConcurrentTaskExecutor concurrentTaskExecutor;

    private long startIndex;
    private long endIndex;
    private long chunkIndex;

    @Resource
    private HttpDownload httpDownload;

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

    public void setter(int threadId, long startIndex, long endIndex) {
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public void setChunkIndex(long chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    @Override
    public String call() throws Exception {
        //  等待所有线程到达
        beginLatch.await();
        //  如果已经取消，直接返回
        if (concurrentTaskExecutor.isCanceled()) {
            //  通知其他线程取消
            endLatch.countDown();
            //  交换数据
            exchanger.exchange(0);
            return String.format("Downloader :%s is given up", threadId);
        }


        RandomAccessFile file = null;


        String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);


        //在目标路径创建文件
        //todo 处理文件不存在时的异常
        file = new RandomAccessFile(getSavePath() + fileName, "rwd");

        //定位文件
        try {
            synchronized (file) {
                file.seek(startIndex);
                int len = 0;
                //读取从startIndex到endIndex的内容
                byte[] buff = httpDownload.readChunk(startIndex,endIndex);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buff);

                byte[] readBuffer = new byte[1024*1024];
                while ((len = byteArrayInputStream.read(readBuffer)) != -1) {
                    file.write(buff, 0, len);
                    ConcurrentTaskExecutor.progress += len;
                }
                writeCompletedChunkToScoreboard(chunkIndex);
            }
        } catch (Exception e) {
            log.error("读写error" + e);
        }


        System.out.println("线程：" + threadId + "号下载完毕了");


        long millis = (long) (Math.random() * 10 * 1000);
        String result = String.format("Player :%s arrived, use %s millis", threadId, millis);
        Thread.sleep(millis);
        System.out.println(result);
        System.out.println("线程：" + threadId + "号下载完毕了");
        exchanger.exchange(1);
        endLatch.countDown();
        return result;
    }


    private void writeCompletedChunkToScoreboard(long chunkIndex) {
        File scoreboardFile = new File(getSavePath(), "scoreboard.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreboardFile, true))) {
            writer.write(Long.toString(chunkIndex));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入任务记分板文件失败");
        }
    }

}

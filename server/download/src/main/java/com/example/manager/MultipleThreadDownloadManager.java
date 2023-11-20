package com.example.manager;

import com.example.common.DownLoader;
import com.example.service.RangeDownload;
import com.example.service.SpeedListener;

import javax.annotation.Resource;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class MultipleThreadDownloadManager extends DownLoader {
    //线程下载数量
    public static int threadCount=1;
    //记录子线程数量
    public static int runningThread=1;
    //文件总大小
    public volatile static long len=0;
    //文件进度
    public volatile static int progress;

    @Resource
    private RangeDownload rangeDownload;

    public MultipleThreadDownloadManager(String urlPath, String savePath) {
        super(urlPath, savePath);
    }

    public void setThreadCount(int threadCount){
        MultipleThreadDownloadManager.threadCount=threadCount;
        runningThread=threadCount;
    }


    @Override
    public void run() {
        try {
//            rangeDownload.download(getUrlPath(),getSavePath());
              long length = rangeDownload.getTotalFileSize(getUrlPath());
              MultipleThreadDownloadManager.len = length;
              long chunkSize = rangeDownload.calculateChunkSize(length);
              rangeDownload.downloadInChunks(getUrlPath(), getTempPath(),length,chunkSize);
              long blockSize = length/threadCount;
//            URL url = new URL(getUrlPath());
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(100);
//            conn.setRequestMethod("GET");
//            if (conn.getResponseCode() == 200) {
//                //服务器返回的数据的长度
//                int length = conn.getContentLength();
//                MultipleThreadDownloadManager.len=length;
//                System.out.println(length);
//                //在客户端本地创建一个大小跟服务器创建一个大小和服务器相等的临时文件
//                RandomAccessFile raf = new RandomAccessFile(getSavePath(), "rwd");
//                //指定创建的这个文件的长度
//                raf.setLength(length);
//                raf.close();
                //计算平均每个线程下载的文件的大小
//                int blockSize = length /threadCount;
                for(int i = 0 ; i < threadCount ; i++){
                    //第一个线程下载的开始位置
                    long startIndex = i * blockSize;
                    //结束位置
                    long endIndex = (i == threadCount - 1) ? length - 1 : (i + 1) * blockSize - 1;
                    //最后一个线程结束位置是文件末尾
                    if(i==threadCount){
                        endIndex=length;
                    }
                    System.out.println("线程："+i+"下载"+startIndex+"--->"+endIndex);
                    SubThreadDownload sonThreadDownload = new SubThreadDownload(getUrlPath(), getSavePath());
                    sonThreadDownload.setBpDownload(this.isBpDownload());
                    sonThreadDownload.setter(i,startIndex,endIndex);
                    //MyExecutorService.getThread().submit(sonThreadDownload);
                    new Thread(sonThreadDownload).start();
                }
                //开始监听下载进度
                speed();
            } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void speed() {
        int temp=0;
        //循环监控网速，如果下载进度达到100%就结束监控
        while(MultipleThreadDownloadManager.progress!= MultipleThreadDownloadManager.len) {
            //System.out.println("ThreadDownload.progress="+ThreadDownload.progress+"--ThreadDownload.len="+ThreadDownload.len);
            temp=progress;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //当前下载进度除以文件总长得到下载进度
            double p=(double)temp/(double)len*100;
            //当前下载进度减去前一秒的下载进度就得到一秒内的下载速度
            temp= progress-temp;

            speedListener.speed(temp,p);
        }
        speedListener.speed(temp,100);
        System.out.println("文件下载完毕");
    }

    SpeedListener speedListener = null;
    /**
     *
     * @param speedListener 网速监听回调接口
     */
    public void addSpeedListener(SpeedListener speedListener){
        this.speedListener=speedListener;
    }

}

package com.example.manager;

import com.example.common.DownLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class SubThreadDownload extends DownLoader {

    private int threadId;
    private long startIndex;
    private long endIndex;

    public SubThreadDownload(String urlPath, String savePath) {
        super(urlPath, savePath);
    }

    public void setter(int threadId,long startIndex,long endIndex){
        this.threadId=threadId;
        this.startIndex=startIndex;
        this.endIndex=endIndex;
    }

    @Override
    public void run() {
        InputStream is=null;
        RandomAccessFile raf=null;
        try {
            //检查是否存在记录下载长度的文件，如果存在就读取这个文件的数据
            File tempFile = new File(getTempPath()+threadId + ".temp");
            //检查是否开启断点继续下载
            if(this.isBpDownload()&&tempFile.exists()&&tempFile.length()>0) {
                FileInputStream fis = new FileInputStream(tempFile);
                byte[] temp = new byte[1024];
                int leng = fis.read(temp);
                fis.close();
                String s = new String(temp, 0, leng);
                int dowloadlenInt = Integer.parseInt(s)-1;
                //修改下载的开始位置
                startIndex+=dowloadlenInt;
                //System.out.println(threadId+"线程真是开始位置："+startIndex);
            }

            URL url = new URL(getUrlPath());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(100);
            conn.setRequestMethod("GET");
            //请求服务器下载部分文件的指定位置的位置
            conn.setRequestProperty("Range","bytes="+startIndex+"-"+endIndex);
            //请求服务器全部资源200ok，如果从服务器请求部分资源206ok
            int responseCode = conn.getResponseCode();
            if(responseCode==206){
                raf = new RandomAccessFile(getSavePath(), "rwd");
                is = conn.getInputStream();
                //定位文件从哪个位置开始写
                raf.seek(startIndex);
                int len=0;
                byte[] buff=new byte[1024*1024];
                //已经下载的数据长度
                int total=0;

                while((len=is.read(buff))!=-1){

                    raf.write(buff,0,len);
                    synchronized (MultipleThreadDownloadManager.class) {
                        MultipleThreadDownloadManager.progress += len;
                    }
                    total+=len;
                    if(isBpDownload()) {
                        //以文件名加线程id保存为临时文件，保存当前线程的下载进度
                        RandomAccessFile info = new RandomAccessFile(getTempPath()  + threadId + ".temp", "rwd");
                        info.write(String.valueOf(total + startIndex).getBytes());
                        info.close();
                    }
                }


            }
            System.out.println("线程："+threadId+"号下载完毕了");
            if(isBpDownload()) {
                synchronized (MultipleThreadDownloadManager.class) {
                    //下载中的线程--，当减到0时代表整个文件下载完毕，如果中途异常，那么这个文件就没下载完
                    MultipleThreadDownloadManager.runningThread--;
                    if (MultipleThreadDownloadManager.runningThread == 0) {
                        for (int i = 0; i < MultipleThreadDownloadManager.threadCount; i++) {
                            File file = new File(getTempPath() + (i + 1) + ".temp");
                            file.delete();
                            System.out.println( (i + 1) + ".txt下载完毕，清除临时文件");
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(threadId+"号线程炸了");
        }finally {

            if(raf!=null){
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

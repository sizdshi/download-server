package com.example.service.impl;

import com.example.service.HttpDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Service
public class HttpDownloadImpl implements HttpDownload {


    private String url;

    public HttpDownloadImpl(){

    }

    public HttpDownloadImpl(String url){
        this.url = url;
    }

    //建立普通get请求，获取文件大小


    @Override
    public long getFileSize() {
        try {
            URL netUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();

            //发送一个普通的GET请求，只获取文件大小而不读取内容
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");

            long totalFileSize = connection.getContentLengthLong();

            connection.disconnect();
            return totalFileSize;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override

    public byte[] readChunk(long start, long end) {
        try {
            URL fileUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setConnectTimeout(100);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);

            int responseCode =  connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_PARTIAL){
                //自动关闭输入输出流
                try(InputStream inputStream = new BufferedInputStream(connection.getInputStream())){
                    int bufferSize = (int) (end-start+1);
                    byte[] buffer = new byte[bufferSize];
                    //判断是否读取到文件尾
                    int bytesRead;
                    //记录读取位置
                    int totalBytesRead = 0;
                    //读取字符流到指定数组中
                    // inputstream.read()详细看文档
                    while (totalBytesRead < bufferSize && (bytesRead = inputStream.read(buffer, totalBytesRead, bufferSize - totalBytesRead)) != -1) {
                        totalBytesRead += bytesRead;
                    }

                    return buffer;
                }

            }else {
                throw new IOException("Unexpected response code: " + responseCode);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 根据分片大小构造 Range 请求进行分片下载

//    public void downloadInChunks(String fileUrl, String savePath, long totalFileSize, long chunkSize) {
//
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
//        RandomAccessFile file = null;
//        try {
//            file = new RandomAccessFile(fileName, "rw");
//            file.setLength(totalFileSize);
//            file.close();
//            for (long startByte = 0; startByte < totalFileSize; startByte += chunkSize) {
//                long endByte = Math.min(startByte + chunkSize - 1, totalFileSize - 1);
//                String rangeHeader = "bytes=" + startByte + "-" + endByte;
//
//                sendRangeRequest(fileUrl, fileName, rangeHeader);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
////        file.seek(0);
//
////        long blockSize = totalFileSize/MultipleThreadDownloadManager.threadCount;
//
//
//    }


//    public void sendRangeRequest(String fileUrl, String savePath, String rangeHeader) {
//
//        URL url = null;
//        try {
//            url = new URL(fileUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("Range", rangeHeader);
//
//            //开始真正发送请求 getInputStream()、getOutputStream() 或 getResponseCode()
//            int responseCode = connection.getResponseCode();
//            System.out.println("响应码" + responseCode);
//
//            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
//
//                InputStream inputStream = connection.getInputStream();
//                RandomAccessFile file = new RandomAccessFile(savePath, "rw");
//
//                // 解析 Range 头部，获取起始字节位置
//                String[] rangeParts = rangeHeader.substring(rangeHeader.indexOf("=") + 1).split("-");
//                long startByte = Long.parseLong(rangeParts[0]);
//                long endByte = Long.parseLong(rangeParts[1]);
//
//                FileChannel channel = file.getChannel();
//                FileLock lock = channel.lock(startByte, endByte, false);
//                if (lock == null) {
//                    throw new IOException("未能获得锁");
//                } else {
//                    synchronized (file) {
//                        file.seek(startByte);
//
//                        byte[] buffer = new byte[1024];
//                        int bytesRead = 0;
//
//                        // 从输入流读取数据，并写入到文件
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            file.write(buffer, 0, bytesRead);
//                        }
//
//                    }
//                    lock.release();
//                }
//
//                file.close();
//                inputStream.close();
//
//                System.out.println("分片成功： " + rangeHeader + "开始位置: " + startByte / 1024 / 1024 + "MB 结束位置：" + endByte / 1024 / 1024 + "MB");
//            } else {
//                System.out.println("Server does not support  range: " + rangeHeader + ". Response code: " + responseCode);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    //    @Override
//    public void download(String filePath, String savePath) {
//
//        long totalTotalFileSize = getTotalFileSize(filePath);
//        System.out.println("文件大小：" + totalTotalFileSize);
//        long chunkSize = calculateChunkSize(totalTotalFileSize);
//        System.out.println("分片大小:" + chunkSize / 1024 / 1024 + "MB");
//        //建立线程池
//        downloadInChunks(filePath, savePath, totalTotalFileSize, chunkSize);
//
//        System.out.println("File downloaded successfully!");
//
//
//    }

    //根据文件大小计算分片大小
//    @Override
//    public long calculateChunkSize(long totalFileSize) {
//        if (totalFileSize <= 32 * 1024) {
//            return 32 * 1024;  // 最小32KB
//        } else if (totalFileSize <= 10 * 1024 * 1024) {
//            return 1 * 1024 * 1024;  // 1MB
//        } else if (totalFileSize <= 100 * 1024 * 1024) {
//            return 10 * 1024 * 1024;  // 10MB
//        } else {
//            // 超过100MB的按照10MB进行分片
//            return 10 * 1024 * 1024;
//        }
//    }
}

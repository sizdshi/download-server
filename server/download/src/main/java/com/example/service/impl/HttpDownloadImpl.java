package com.example.service.impl;

import com.example.service.HttpDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.*;
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
    public long getFileSize() throws IOException{
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        int port = netUrl.getPort() != -1 ? netUrl.getPort() : 80;
        String path = netUrl.getPath();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 发送HTTP请求
            pw.println("HEAD " + path + " HTTP/1.1");
            pw.println("Host: " + host);
            pw.println("Accept: */*");
            pw.println("Connection: Close");
            pw.println(""); // HTTP请求头和消息体之间需要一个空行
            pw.flush();

            // 读取响应
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                if (responseLine.startsWith("Content-Length:")) {
                    long contentLength = Long.parseLong(responseLine.substring(16).trim());
                    return contentLength;
                }
            }
        }
        throw new IOException("Content-Length not found");
    }


    @Override
    public byte[] readChunk(long start, long end) throws IOException {
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        int port = netUrl.getPort() != -1 ? netUrl.getPort() : 80;
        String path = netUrl.getPath();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            InputStream in = socket.getInputStream();

            // 发送HTTP请求
            pw.println("GET " + path + " HTTP/1.1");
            pw.println("Host: " + host);
            pw.println("Range: bytes=" + start + "-" + end);
            pw.println("Accept: */*");
            pw.println("Connection: Close");
            pw.println(""); // HTTP请求头和消息体之间需要一个空行
            pw.flush();

            // 跨过HTTP响应头
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                if (responseLine.isEmpty()) { // 当读到空行时，表明响应头部分结束
                    break;
                }
            }

            // 读取指定的字节范围
            int bufferSize = (int) (end - start + 1);
            byte[] buffer = new byte[bufferSize];
            DataInputStream dis = new DataInputStream(in);

            dis.readFully(buffer);
            return buffer;
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

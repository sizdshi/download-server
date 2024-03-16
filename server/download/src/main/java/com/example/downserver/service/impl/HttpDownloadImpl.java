package com.example.downserver.service.impl;

import com.example.downserver.service.HttpDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Service
public class HttpDownloadImpl implements HttpDownload {

    private final static Logger log = LoggerFactory.getLogger(HttpDownloadImpl.class);
    private String url;

    public HttpDownloadImpl() {
        this(null);

    }

    public HttpDownloadImpl(String url) {
        this.setUrl(url);
        this.url = url;
    }


    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取指定URL的文件大小。
     * 该方法通过发送一个GET请求来获取服务器上文件的大小，单位为字节。
     *
     * @return 返回文件的大小，如果无法获取则抛出RuntimeException。
     */
    @Override
    public long getFileSize() {
        try {
            URL netUrl = new URL(url); // 创建URL对象
            HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection(); // 打开连接

            // 设置请求方法为GET，只获取文件大小而不读取文件内容
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100); // 设置连接超时时间
            connection.setRequestProperty("accept", "*/*"); // 设置接受所有类型的内容
            connection.setRequestProperty("connection", "keep-alive"); // 设置保持连接
            connection.setRequestProperty("user-agent", // 设置用户代理，模拟浏览器请求
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");

            long totalFileSize = connection.getContentLengthLong(); // 获取文件大小
            connection.disconnect(); // 断开连接
            return totalFileSize;
        } catch (IOException e) {
            // 如果发生IO异常，则抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定URL读取指定范围的数据。
     *
     * @param start 起始读取位置（字节） 从0开始
     * @param end   结束读取位置（字节）
     * @return 从网络读取的字节数组
     * @throws RuntimeException 当读取发生错误或响应码非预期时抛出
     * @Author zyh
     */
    @Override
    public byte[] readChunk(long start, long end) {
        try {
            // 创建URL对象并打开连接
            URL fileUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setConnectTimeout(100); // 设置连接超时时间
            connection.setRequestMethod("GET"); // 设置请求方法为GET
            // 设置请求头，请求特定范围的数据
            connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
            int responseCode = connection.getResponseCode(); // 获取响应码
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                // 当响应码表示部分内容时，即请求的范围数据成功
                try (InputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                    int bufferSize = (int) (end - start); // 计算缓冲区大小
                    byte[] buffer = new byte[bufferSize]; // 创建字节数组
                    // 读取数据到缓冲区
                    int bytesRead;
                    if ((bytesRead = inputStream.read(buffer, 0, bufferSize)) != -1) {
                        // 成功读取数据
                        log.info(">>>>> read success!");
                        log.info("byteRead: " + bytesRead + " \t bufferSize:" + bufferSize);
                        if (bytesRead != bufferSize) {
                            // 如果实际读取的字节数小于预期，说明已到达文件末尾
                            log.info(">>>>> read EOF but success! ");
                            return Arrays.copyOf(buffer, bytesRead);
                        }
                        return buffer; // 返回读取的字节数组
                    } else {
                        // 如果未能读取到任何数据，抛出运行时异常
                        throw new RuntimeException("read error! EOF");
                    }
                }

            } else {
                // 如果响应码非预期，抛出IO异常
                throw new IOException("Unexpected response code: " + responseCode);
            }

        } catch (Exception e) {
            // 捕获并抛出所有异常
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

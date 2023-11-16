package com.example.service.impl;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@Data
public class UnitDownloader implements Runnable{
//    private int from;
//    private int to;
//    private File target;
//    private String uri;
//    private int id;
//
//    public UnitDownloader(int from, int to, File target, String uri, int id) {
//        this.from = from;
//        this.to = to;
//        this.target = target;
//        this.uri = uri;
//        this.id = id;
//    }

//    public int getFrom() {
//        return from;
//    }
//
//    public int getTo() {
//        return to;
//    }

    @Override
    public void run() {
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
//            connection.setRequestProperty("Range", "bytes=" + from + "-" + to);
//            connection.connect();
//            int totalSize = connection.getContentLength();
//            InputStream inputStream = connection.getInputStream();
//            RandomAccessFile randomAccessFile = new RandomAccessFile(target, "rw");
//            randomAccessFile.seek(from);
//            byte[] buffer = new byte[1024 * 1024];
//            int readCount = inputStream.read(buffer, 0, buffer.length);
//            while (readCount > 0) {
//                totalSize -= readCount;
//                System.out.println("分片：" + this.id + "的剩余：" + totalSize);
//                randomAccessFile.write(buffer, 0, readCount);
//                readCount = inputStream.read(buffer, 0, buffer.length);
//            }
//            inputStream.close();
//            randomAccessFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}

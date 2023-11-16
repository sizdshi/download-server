package com.example.manager;

import com.example.service.impl.UnitDownloader;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class MultipleThreadDownloadManager implements Runnable{
//    private String uri;
//    private File target;
//
//    public MultipleThreadDownloadManager(String uri, File target) {
//        this.target = target;
//        this.uri = uri;
//        if (target.exists() == false) {
//            try {
//                target.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void start() {
//        new Thread(this).start();
//    }
//
//    public int threadCount(int totalSize) {
//        if (totalSize < 30 * 2014 * 1024) {
//            return 1;
//        }
//        return 30;
//    }

    @Override
    public void run() {
//
//        int totalSize = 0;
//        try {
//            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
//            connection.connect();
//            int contentLength = connection.getContentLength();
//            totalSize = contentLength;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int threadCount = threadCount(totalSize);
//        int perThreadSize = totalSize / threadCount;//每一个线程分到的任务下载量
//        int id = 0;
//        int from = 0, to = 0;
//        while (totalSize > 0) {
//            id++;
//            //计算分片
//            if (totalSize < perThreadSize) {
//                from = 0;
//                to = totalSize;
//            } else {
//                from = totalSize;
//                to = from + perThreadSize;
//            }
//
//            UnitDownloader downloader = new UnitDownloader(from, to, target, uri, id);
//            new Thread(downloader).start();
//        }
    }
}

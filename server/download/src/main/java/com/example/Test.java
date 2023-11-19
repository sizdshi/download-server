package com.example;

import com.example.service.HttpDownloadService;
import com.example.service.impl.HttpDownloadServiceImpl;

public class Test {
    public static void main(String[] args) {

        HttpDownloadService service = new HttpDownloadServiceImpl();
        String filePath = service.download("https://img1.baidu.com/it/u=1106510146,2581987158&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800");

    }
}

package com.example.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@RestController
@RequestMapping("/download")
public class HttpController {


    String fileUrl = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    String savePath = "QQ9.7.19.29259.exe";



    @GetMapping("/start")
    public void fileDownload() {


//        MultipleThreadDownloadManager threadDownload = new MultipleThreadDownloadManager(fileUrl, "L:\\test\\");
//        //设置线程数
//
//        threadDownload.setThreadCount(4);
//
//        //添加进度和网速监听
//        threadDownload.addSpeedListener((s, progress) -> {
//            String m = String.format("%.2f", (double) s / 1024 / 1024);
//            String pro = String.format("%.2f", progress);
//            System.out.println(m + "m/s--进度：" + pro + "%");
//        });
//
//        threadDownload.run();

    }


}

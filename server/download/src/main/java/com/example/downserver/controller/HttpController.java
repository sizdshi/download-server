package com.example.downserver.controller;

import com.example.download.common.ErrorCode;
import com.example.download.utils.ResultUtils;
import com.example.downserver.manager.ConcurrentTaskExecutor;
import com.example.downserver.model.dto.SendRequest;
import com.example.exception.BusinessException;
import com.example.model.BaseResponse;
import org.apache.commons.lang3.StringUtils;
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


    @PostMapping("/start")
    public BaseResponse<Object> startDownload(@RequestBody SendRequest sendRequest) {


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
        if(!StringUtils.isNotEmpty(sendRequest.getUrl())|| !StringUtils.isNotEmpty(sendRequest.getSavePath())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }


        ConcurrentTaskExecutor executor2 = new ConcurrentTaskExecutor();
        executor2.setUrlPath(sendRequest.getUrl());
        executor2.setSavePath(sendRequest.getSavePath());
        executor2.setThreadCount(6);
        executor2.addSpeedListener((speed,progress)->{
            String m = String.format("%.2f",  speed / 1024 / 1024);
            String pro = String.format("%.2f", progress);
            System.out.println(m + "m/s--进度：" + pro + "%");
        });
        try {
            executor2.executeTask();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("--------------------------------开始下载");
        return ResultUtils.success("开始下载");
    }


}

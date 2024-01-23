package com.example.downserver.controller;

import com.example.download.common.ErrorCode;
import com.example.download.utils.ResultUtils;
import com.example.downserver.manager.ConcurrentTaskExecutor;
import com.example.downserver.model.dto.SendRequest;
import com.example.exception.BusinessException;
import com.example.model.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

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

        if(!StringUtils.isNotEmpty(sendRequest.getUrl())|| !StringUtils.isNotEmpty(sendRequest.getSavePath())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }


        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
        executor.setUrlPath(sendRequest.getUrl());
        executor.setSavePath(sendRequest.getSavePath());
        executor.setThreadCount(sendRequest.getThreadCount());
        executor.addSpeedListener((speed,progress)->{
            String m = String.format("%.2f",  speed / 1024 / 1024);
            String pro = String.format("%.2f", progress);
            System.out.println(m + "m/s--进度：" + pro + "%");
        });
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    executor.executeTask();
                } catch (Exception e) {
                    // Handle the exception as needed
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("--------------------------------开始下载");
        return ResultUtils.success("开始下载");
    }


}

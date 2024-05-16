package com.example.downserver.controller;


import com.example.download.common.ErrorCode;
import com.example.download.utils.ResultUtils;
import com.example.downserver.config.SseServer;
import com.example.downserver.manager.ConcurrentTaskExecutor;
import com.example.downserver.model.dto.SendRequest;
import com.example.exception.BusinessException;
import com.example.model.BaseResponse;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@RestController
@CrossOrigin
@RequestMapping("/sse")
public class SSEDemoController {
    /**
     * 用户SSE连接
     * 它返回一个SseEmitter实例，这时候连接就已经创建了.
     *
     * @return
     */
    @GetMapping("/userConnect")
    public SseEmitter connect(@RequestParam("userId") String userId) {
        /**
         * 取登录用户账号作为 messageId。
         * 分组的话需要约定 messageId的格式。
         * 这里模拟创建一个用户连接
         */
//        String userId = "userId-" + RandomUtils.nextInt(1, 10);
        try {
            return SseServer.createConnect(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 模拟实例：下载进度条显示。 前端访问下载接口之前，先建立用户SSE连接，然后访问下载接口，服务端推送消息。
     *
     *
     * @throws InterruptedException
     */
    @GetMapping("/downLoad/{userId}")
    public void pushOne(@PathVariable("userId") String userId) throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            if (i > 50 && i < 70) {
                Thread.sleep(500L);
            } else {
                Thread.sleep(100L);
            }
            System.out.println("sendMessage --> 消息=" + i);
            SseServer.sendMessage(userId, String.valueOf(i));
        }
        System.out.println("下载成功");
    }

    @PostMapping("/downLoad/{userId}")
    public BaseResponse<Object> pushOne(@RequestBody SendRequest sendRequest, @PathVariable("userId") String userId) throws InterruptedException {

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
            SseServer.sendMessage(userId,pro);
            System.out.println(m + "m/s--进度：" + pro + "%");
        });
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    executor.executeTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("--------------------------------开始下载");
        return ResultUtils.success("开始下载");

    }


    /**
     * 广播发送。http://localhost:8080/sse/pushAllUser
     *
     * @throws InterruptedException
     */
    @GetMapping("/pushAllUser")
    public void pushAllUser() throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            if (i > 50 && i < 70) {
                Thread.sleep(500L);
            } else {
                Thread.sleep(100L);
            }
            System.out.println("batchAllSendMessage --> 消息=" + i);
            SseServer.batchAllSendMessage(String.valueOf(i));
        }
    }
}
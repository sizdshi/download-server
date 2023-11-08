package com.example.downloadserver.controller;

import com.example.downloadserver.common.ErrorCode;
import com.example.downloadserver.exception.BusinessException;
import com.example.downloadserver.model.BaseResponse;
import com.example.downloadserver.service.TransferService;
import com.example.downloadserver.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@RestController
@Slf4j
@RequestMapping("/task")
public class TransferController {

    int corePoolSize = 5;
    int maximumPoolSize = 10;
    long keepAliveTime = 60L;

    TimeUnit unit = TimeUnit.SECONDS;
    @Resource
    private TransferService transferService;

    public  static boolean isPaused = false;

    @PostMapping("/thread")
    public BaseResponse<Object> changeThread(@RequestParam("id")String id, @RequestParam("num") int num, HttpServletRequest request){
        //检查id是否规范
        if(!StringUtils.isNotEmpty(id) || !StringUtils.isNotEmpty(String.valueOf(num))){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //todo 检查文件是否在数据库中存在


        //todo 线程池里加减线程,待封装
        //ExecutorService executor = Executors.newFixedThreadPool(5);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        System.out.println("修改之前的线程数：");
        System.out.println("核心线程数：" + executor.getCorePoolSize());
        //异步执行
       Future<?> downloadResult = executor.submit(()->{
            //todo 下载服务
            Task task = new Task(id);
            task.run();
            System.out.println("这是"+id);
        });
        if(!downloadResult.isDone()){
            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executor;
            threadPool.setMaximumPoolSize(num);
            System.out.println("当前使用 " + num + " 个线程来加速下载...");
        }

        executor.shutdown();
//        BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
//
//        executor.execute(new Task(id));
////        int newMaximumPoolSize = num;
//        executor.setMaximumPoolSize(num);

        String result = "success";

        return ResultUtils.success(result);
    }

    @PostMapping("/start")
    public BaseResponse<Object> start(@RequestParam("id")String id,HttpServletRequest request){
        //todo 检查参数

        //todo 检查是否有缓存数据，如果有，继续下载，如果没有，重新下载
        // 在循环中判断是否需要继续上传
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("Upload paused.");
            // 执行暂停上传时的逻辑

        } else {
            System.out.println("Resuming upload.");
            // 执行继续上传时的逻辑
        }

         if(!isPaused){
             //开始下载，查找状态？
             System.out.println("开始下载");
         }

//        while(true){
//            if (isPaused){
//                //跳过发送数据xxx
//                continue;
//            }
//        }

        //
        String result = "success";

        return ResultUtils.success(result);
    }

    @PostMapping("/stop")
    public BaseResponse<Object> stop(@RequestParam("id")String id,HttpServletRequest request){

        //todo 检查参数
        //todo 下载的线程停止 ？ 全局的停止


        String result = "success";
        return ResultUtils.success(result);
    }
    @PostMapping("/delete")
    public BaseResponse<Object> delete(@RequestParam("id")String id,HttpServletRequest request){


        String result = "success";
        return ResultUtils.success(result);
    }
    @PostMapping("/suspend")
    public BaseResponse<Object> suspend(@RequestParam("id")String id,HttpServletRequest request){

        String result = "success";
        return ResultUtils.success(result);
    }


    private static class Task implements Runnable{
        private String taskId;

        public Task(String  taskId){
            this.taskId = taskId;
        }

        @Override
        public void run() {
            System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getId());
        }
    }

    public void setPaused(boolean paused){
        isPaused = paused;
    }


}

package com.example;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@SpringBootApplication
@EnableScheduling
public class Download {

//    @DubboReference
//    private InnerDownloadService innerDownloadService;

//    @DubboReference
//    private InnerDownloadService innerDownloadService;
//
//    @PostConstruct
//    public void init(){
//        String test = innerDownloadService.say("123");
//
//        String test2 = innerDownloadService.hello();
//
//        System.out.println(test+"\n"+test2);
//    }

    public static void main(String[] args) {
       SpringApplication.run(Download.class);
    }

//    @PostConstruct
//    public void init(){
//        downloadJob.searchStatus();
//    }
}
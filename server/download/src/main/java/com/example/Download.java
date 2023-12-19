package com.example;


import com.example.manager.ConcurrentTaskExecutor;
import com.example.service.HttpDownload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Download {
    private static String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    private static String filePath = "L:\\test\\";

    @Autowired
    private ConcurrentTaskExecutor concurrentTaskExecutor;


    public static void main(String[] args) {
        SpringApplication.run(Download.class);

        try {
            ConcurrentTaskExecutor executor2 = new ConcurrentTaskExecutor("https://dldir1v6.qq.com/weixin/Windows/WeChatSetup.exe", filePath, 6);
            executor2.executeTask();
            ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(url, filePath, 5);
            executor.executeTask();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
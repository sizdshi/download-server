package com.example;



import com.example.manager.ConcurrentTaskExecutor;
import com.example.service.HttpDownload;
import com.example.service.impl.HttpDownloadImpl;
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
    private static String url="https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    private static String filePath="L:\\test\\";
    public static void main(String[] args) {
        SpringApplication.run(Download.class);
        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(url,filePath,5);
        executor.executeTask();
    }


}
package com.example;



import com.example.manager.ConcurrentTaskExecutor;
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
    private static String url="https://dldir1.qq.com/qqfile/qq/QQNT/525d0565/QQ_v6.9.23.19189.dmg";
    private static String filePath="/Users/hcshi/Downloads/test/";
    public static void main(String[] args) {
        SpringApplication.run(Download.class);
        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(url,filePath,5);
        try {
            executor.executeTask();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
package com.example;



import com.example.manager.ConcurrentTaskExecutor;
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

    public static void main(String[] args) {
       SpringApplication.run(Download.class);
        ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor();
        executor.executeTask();
    }


}
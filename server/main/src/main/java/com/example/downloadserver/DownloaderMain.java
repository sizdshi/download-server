package com.example.downloadserver;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@SpringBootApplication
@EnableDubbo
@MapperScan("com.example.downloadserver.mapper")
public class DownloaderMain {

    public static void main(String[] args) {
        SpringApplication.run(DownloaderMain.class);
    }
}
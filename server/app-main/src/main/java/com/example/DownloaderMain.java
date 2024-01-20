package com.example;



import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@SpringBootApplication
@MapperScan("com.example.mapper")
@Slf4j
public class DownloaderMain {
    @PostConstruct
    public void init() {
        // 在这里添加初始化日志输出或其他初始化逻辑
        log.info("Module app-main initialized!");
        System.out.println("Module app-main initialized!");
    }
    public static void main(String[] args) {
        SpringApplication.run(DownloaderMain.class,args);
    }
}
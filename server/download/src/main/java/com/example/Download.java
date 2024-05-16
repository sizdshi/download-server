package com.example;

import com.example.downserver.manager.ConcurrentTaskExecutor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@SpringBootApplication
@MapperScan("com.example.downserver.mapper")
@EnableScheduling
public class Download {
    private static String url = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    private static String filePath = "L://test//";
    private static String dmgurl = "https://dldir1.qq.com/qqfile/qq/QQNT/cc446c00/QQ_v6.9.23.19689.dmg";
    private static String weixinurl = "https://dldir1.qq.com/weixin/mac/WeChatMac.dmg";

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Download.class, args);
    }

}
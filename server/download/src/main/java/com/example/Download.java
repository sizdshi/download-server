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
//        SpringApplication.run(Download.class,args);
        ApplicationContext context = SpringApplication.run(Download.class, args);
//        try {
//
////            ConcurrentTaskExecutor executor2 = new ConcurrentTaskExecutor(url, filePath, 6);
//            ConcurrentTaskExecutor executor2 = context.getBean(ConcurrentTaskExecutor.class);
//            executor2.setUrlPath(url);
//            executor2.setSavePath(filePath);
//            executor2.setThreadCount(6);
//            executor2.addSpeedListener((speed,progress)->{
//                String m = String.format("%.2f",  speed / 1024 / 1024);
//                String pro = String.format("%.2f", progress);
//                System.out.println(m + "m/s--进度：" + pro + "%");
//            });
//            executor2.executeTask();
////            ConcurrentTaskExecutor executor = new ConcurrentTaskExecutor(dmgurl, filePath, 5);
////            executor.addSpeedListener((speed,progress)->{
////                String m = String.format("%.2f",  speed / 1024 / 1024);
////                String pro = String.format("%.2f", progress);
////                System.out.println(m + "m/s--进度：" + pro + "%");
////            });
////            executor.executeTask();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }



}
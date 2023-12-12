package com.example;

import com.example.service.DownloadService;
import com.example.manager.MultipleThreadDownloadManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@SpringBootTest
public class SpringTest {
    String fileUrl = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    String savePath = "QQ9.7.19.29259.exe";

    @Resource
    private DownloadService downloadService;
    @Test
    void contextLoads() {

    }

    @Test
    void MultipleTest() {

        String id = downloadService.submit(fileUrl);
        System.out.println("传入任务id"+id);
        List<String> ids = new ArrayList<>();

        MultipleThreadDownloadManager threadDownload = new MultipleThreadDownloadManager(fileUrl, "L:\\test\\");
        //设置线程数

        threadDownload.setThreadCount(4);

        //添加进度和网速监听
        threadDownload.addSpeedListener((s, progress) -> {
            String m = String.format("%.2f", (double) s / 1024 / 1024);
            String pro = String.format("%.2f", progress);
            System.out.println(m + "m/s--进度：" + pro + "%");
        });

        threadDownload.run();

        System.out.println(downloadService.suspend(ids));

    }

}

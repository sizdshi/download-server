package com.example;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.downserver.model.entity.Download;
import com.example.downserver.service.DownloadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.example.Download.class)
public class DownloadServiceTest {

    @Autowired
    private DownloadService downloadService;

    @Test
    public void testDownloadService() {
        // 执行测试逻辑，调用 downloadService 的方法
        System.out.println("DownloadServiceTest.testDownloadService");
        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Download::getUrl, "https://dldir1.qq.com/qqfile/qq/QQ9.7.19/29259/QQ9.7.19.29259.exe");
        lambdaQueryWrapper.eq(Download::getIs_delete, 0);
        Download download = downloadService.getOne(lambdaQueryWrapper);

        System.out.println(download);
    }
}

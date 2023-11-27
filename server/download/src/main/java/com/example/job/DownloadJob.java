package com.example.job;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.downloadserver.mapper.DownloadMapper;
import com.example.manager.MultipleThreadDownloadManager;
import com.example.service.inner.InnerDownloadService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Component
public class DownloadJob {

//    @Resource
//    private DownloadMapper downloadMapper;
//
//    private MultipleThreadDownloadManager manager;
//
//    @Autowired
//    public DownloadJob(MultipleThreadDownloadManager manager){
//        this.manager = manager;
//    }
//
//    @Scheduled(fixedDelay = 200)
//    public void searchStatus(){
//        LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq()
//    }


}

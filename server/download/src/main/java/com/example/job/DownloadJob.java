package com.example.job;

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



    @Scheduled(fixedRate = 100)
    public void searchStatus(){
        return;
    }
}

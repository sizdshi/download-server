package com.example.downserver.job;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.download.common.ErrorCode;
import com.example.downserver.model.entity.Download;
import com.example.downserver.service.DownloadService;
import com.example.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Component
public class DownloadJob {

//    @Autowired
//    private DownloadService downloadService;
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
//        LambdaQueryWrapper<Download> lambdaQueryWrapper = new LambdaQueryWrapper();
//        lambdaQueryWrapper.eq(Download::getUrl, "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe");
//        lambdaQueryWrapper.eq(Download::getIs_delete,0);
//        Download download = downloadService.getOne(lambdaQueryWrapper);
//
//        if (download == null) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未获取到Download对象");
//        }
//
//        String taskStatus = download.getStatus();
//        System.out.println("当前任务状态： "+taskStatus);
//    }
//



}

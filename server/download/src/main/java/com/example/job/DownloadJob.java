package com.example.job;



import com.example.service.inner.InnerDownloadService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;


/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Component
public class DownloadJob {

//    @Resource
   @DubboReference
   private InnerDownloadService innerDownloadService;


//    @Scheduled(fixedRate = 100)
    public void searchStatus(){
        innerDownloadService.searchAllTaskStatus();
    }


}

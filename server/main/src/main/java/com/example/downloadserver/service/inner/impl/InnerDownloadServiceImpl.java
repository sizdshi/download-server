package com.example.downloadserver.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.example.service.inner.InnerDownloadService;
import com.example.downloadserver.mapper.DownloadMapper;
import com.example.downloadserver.model.entity.Download;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class InnerDownloadServiceImpl implements InnerDownloadService{
    @Resource
    private DownloadMapper downloadMapper;



    @Override
    public Map<String, String> searchAllTaskStatus() {
        Map<String,String> idStatusMap = new HashMap<>();
        LambdaQueryWrapper<Download> queryWrapper = new LambdaQueryWrapper<>();
//        List<Download> downloads = list(queryWrapper);
//        for(Download download : downloads){
//            idStatusMap.put()
//        }

        return null;
    }

    @Override
    public String say(String text) {
        System.out.println("Hello world" + text);
        return text + "hello";
    }

    @Override
    public String hello() {
        System.out.println("你好");
        return "测试";
    }


}

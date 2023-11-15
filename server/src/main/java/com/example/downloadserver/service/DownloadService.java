package com.example.downloadserver.service;

import com.example.downloadserver.model.entity.Download;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Service
* @createDate 2023-11-09 14:40:30
*/
public interface DownloadService extends IService<Download> {

    long changeThread(String id, long num, HttpServletRequest request);

    long start(String id,HttpServletRequest request);

    long suspend(String id, HttpServletRequest request);

    long stop(String id, HttpServletRequest request);

    long delete(String id);

    long submit(String url);

}

package com.example.downloadserver.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.downloadserver.model.dto.DownloadRequest;
import com.example.downloadserver.model.vo.DownloadVO;
import com.example.model.Download;

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

    Page<DownloadVO> getDownloadVOPage(Page<Download> downloadPage,HttpServletRequest request);

    Page<DownloadVO> listDownloadVOByPage(DownloadRequest downloadRequest,HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param downloadRequest
     * @return
     */
    QueryWrapper<Download> getQueryWrapper(DownloadRequest downloadRequest);

}

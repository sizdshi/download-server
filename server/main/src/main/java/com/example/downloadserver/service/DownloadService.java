package com.example.downloadserver.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.downloadserver.model.dto.DownloadRequest;
import com.example.downloadserver.model.dto.ThreadRequest;
import com.example.downloadserver.model.entity.Download;
import com.example.downloadserver.model.vo.DownloadVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author sizd-shi
* @description 针对表【download(上传下载表)】的数据库操作Service
* @createDate 2023-11-09 14:40:30
*/
public interface DownloadService extends IService<Download> {

    long changeThread(ThreadRequest threadRequest, HttpServletRequest request);

    long start(List<String> ids,HttpServletRequest request);

    long suspend(List<String> ids, HttpServletRequest request);

    long restart(List<String> ids, HttpServletRequest request);

    long delete(List<String> ids);

    String submit(String url);



    Page<DownloadVO> getDownloadVOPage(Page<Download> downloadPage,HttpServletRequest request);

    Page<DownloadVO> listDownloadVOByPage(DownloadRequest downloadRequest,HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param downloadRequest
     * @return
     */
    QueryWrapper<Download> getQueryWrapper(DownloadRequest downloadRequest);

    long suspend(List<String> ids);

}

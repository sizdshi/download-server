package com.example.downloadserver.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.ErrorCode;
import com.example.downloadserver.model.dto.DownloadRequest;
import com.example.downloadserver.model.dto.FilterRequest;
import com.example.downloadserver.model.dto.SubmitRequest;
import com.example.downloadserver.model.dto.ThreadRequest;
import com.example.downloadserver.model.entity.Download;
import com.example.downloadserver.model.vo.DownloadVO;
import com.example.exception.BusinessException;
import com.example.model.BaseResponse;
import com.example.downloadserver.service.DownloadService;
import com.example.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@RestController
@Slf4j
@RequestMapping("/task")
public class TransferController {


    @Resource
    private DownloadService downloadService;


    public static boolean isPaused = false;

    @PostMapping("/thread")
    public BaseResponse<Object> changeThread(@RequestBody ThreadRequest threadRequest, HttpServletRequest request) {
        //检查id是否规范
        if (!StringUtils.isNotEmpty(threadRequest.getId()) || !StringUtils.isNotEmpty(String.valueOf(threadRequest.getCount()))) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        long result = downloadService.changeThread(threadRequest, request);
        return ResultUtils.success(result);
    }


    @PostMapping("/start")
    public BaseResponse<Object> start(@RequestBody List<String> ids, HttpServletRequest request) {
        //todo 检查参数
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        long result = downloadService.start(ids, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/restart")
    public BaseResponse<Object> restart(@RequestBody List<String> ids, HttpServletRequest request) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        long result = downloadService.restart(ids, request);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    public BaseResponse<Object> delete(@RequestBody List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        System.out.println("开始执行");
        long result = downloadService.delete(ids);
        return ResultUtils.success(result);
    }

    @PostMapping("/suspend")
    public BaseResponse<Object> suspend(@RequestBody List<String> ids, HttpServletRequest request) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        System.out.println("开始执行");
        long result = downloadService.suspend(ids, request);
        return ResultUtils.success(result);

    }

    @PostMapping("/submit")
    public BaseResponse<Object> submit(@RequestBody SubmitRequest submitRequest) {
        if (!StringUtils.isNotEmpty(submitRequest.getUrl())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String result = downloadService.submit(submitRequest.getUrl());
        return ResultUtils.success(result);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<DownloadVO>> listPostVOByPage(@RequestBody DownloadRequest downloadRequest,
                                                           HttpServletRequest request) {
        long current = downloadRequest.getCurrent();
        long size = downloadRequest.getPageSize();

        Page<Download> downloadPage = downloadService.page(new Page<>(current, size),
                downloadService.getQueryWrapper(downloadRequest));

        return ResultUtils.success(downloadService.getDownloadVOPage(downloadPage, request));
    }

//    @PostMapping("/filter")
//    public BaseResponse<Object> filter(@RequestBody DownloadRequest downloadRequest) {
//        if (!StringUtils.isNotEmpty(filterRequest.getStatus())) {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
//        }
//        String result = downloadService.submit(filterRequest.getStatus()));
//        return ResultUtils.success(result);
//    }






}

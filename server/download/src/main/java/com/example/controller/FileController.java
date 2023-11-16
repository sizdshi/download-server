package com.example.controller;

import com.example.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Controller
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件分片下载
     * @param range http请求头Range，用于表示请求指定部分的内容。
     *              格式为：Range: bytes=start-end  [start,end]表示，即是包含请求头的start及end字节的内容
     * @param request   http请求
     * @param response  http响应
     */
    @RequestMapping(value = "/file/chunk/download", method = RequestMethod.GET)
    public void fileChunkDownload(@RequestHeader(value = "Range") String range,
                                  HttpServletRequest request, HttpServletResponse response) {
        fileService.fileChunkDownload(range,request,response);
    }
}

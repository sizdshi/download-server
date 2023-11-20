package com.example.controller;

import com.example.manager.MultipleThreadDownloadManager;
import com.example.service.RangeDownload;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@RestController
@RequestMapping("/files")
public class FileController {
    @Resource
    private FileService fileService;

    @Resource
    private RangeDownload rangeDownload;

    String fileUrl = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    String savePath = "QQ9.7.19.29259.exe";;


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

    @GetMapping("/download")
    public void fileDownload(){
//        rangeDownload.download(fileUrl,savePath);
//        System.out.println("下载成功");

        //1、连接服务器，获取一个文件，获取文件的长度，在本地创建一个大小跟服务器文件一样大的临时文件
        MultipleThreadDownloadManager threadDownload = new MultipleThreadDownloadManager(fileUrl, "M:\\temp.exe");
        //设置线程数
        threadDownload.setThreadCount(4);
        //开启断点下载
        //threadDownload.setBpDownload(true);
        //添加进度和网速监听
        threadDownload.addSpeedListener((s,progress)->{
            String m=String.format("%.2f",(double) s/1024/1024);
            String pro=String.format("%.2f",progress);
            System.out.println(m+"m/s--进度："+pro+"%");
        });
        //由于ThreadDownload类也是个线程类，可以开启线程
        threadDownload.run();

    }


}

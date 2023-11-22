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


//    @Resource
//    private RangeDownload rangeDownload;

    String fileUrl = "https://dldir1.qq.com/qqfile/qq/PCQQ9.7.19/QQ9.7.19.29259.exe";
    String savePath = "QQ9.7.19.29259.exe";;



//    @RequestMapping(value = "/file/chunk/download", method = RequestMethod.GET)
//    public void fileChunkDownload(@RequestHeader(value = "Range") String range,
//                                  HttpServletRequest request, HttpServletResponse response) {
//        fileService.fileChunkDownload(range,request,response);
//    }

    @GetMapping("/down")
    public void fileDownload(){

        MultipleThreadDownloadManager threadDownload = new MultipleThreadDownloadManager(fileUrl, "M:\\temp\\");
        //设置线程数
        threadDownload.setThreadCount(4);

        //添加进度和网速监听
        threadDownload.addSpeedListener((s,progress)->{
            String m=String.format("%.2f",(double) s/1024/1024);
            String pro=String.format("%.2f",progress);
            System.out.println(m+"m/s--进度："+pro+"%");
        });

        threadDownload.run();

    }


}

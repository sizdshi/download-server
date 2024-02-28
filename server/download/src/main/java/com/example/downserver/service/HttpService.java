package com.example.downserver.service;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface HttpService {

    /**
     * http从链接下载
     * @param url
     * @param fileName
     * @param savePath
     * @return
     */
    String downloadFromUrl(String url,String fileName,String savePath);


}

package com.example.service;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface RangeDownload {

    void download(String filePath,String savePath);

    long getTotalFileSize(String url);

    long calculateChunkSize(long totalFileSize);

    void downloadInChunks(String fileUrl, String savePath, long totalFileSize, long chunkSize);

    void sendRangeRequest(String fileUrl, String savePath, String rangeHeader);
}

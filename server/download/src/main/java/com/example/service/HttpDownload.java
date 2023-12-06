package com.example.service;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface HttpDownload {

//    void download(String filePath,String savePath);

    /**
     * 计算文件大小
     * @return
     */
    long getFileSize();

//    long calculateChunkSize(long totalFileSize);

    /**
     * 读取指定的chunk
     * @param start
     * @param end
     * @return
     */
    byte[] readChunk(long start,long end);

//    void downloadInChunks(String fileUrl, String savePath, long totalFileSize, long chunkSize);
//
//    void sendRangeRequest(String fileUrl, String savePath, String rangeHeader);


}

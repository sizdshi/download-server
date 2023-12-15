package com.example.service;

import java.io.IOException;

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
    long getFileSize() throws IOException;

//    long calculateChunkSize(long totalFileSize);

    /**
     * 读取指定的chunk
     * @param start
     * @param end
     * @return
     */
    byte[] readChunk(long start,long end) throws IOException;

//    void downloadInChunks(String fileUrl, String savePath, long totalFileSize, long chunkSize);
//
//    void sendRangeRequest(String fileUrl, String savePath, String rangeHeader);


}

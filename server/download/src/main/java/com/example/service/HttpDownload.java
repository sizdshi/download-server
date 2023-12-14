package com.example.service;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface HttpDownload {

    /**
     * 计算文件大小
     * @return
     */
    long getFileSize();

    /**
     * 读取指定的chunk
     * @param start
     * @param end
     * @return
     */
    byte[] readChunk(long start,long end);

    void setUrl(String url);
}

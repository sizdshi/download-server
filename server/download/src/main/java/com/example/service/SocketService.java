package com.example.service;

import java.io.IOException;

public interface SocketService {


    /**
     * 计算文件大小
     * @return
     */
    long getFileSize() throws IOException;

    /**
     * 读取指定的chunk
     * @param start
     * @param end
     * @return
     */
    byte[] readChunk(long start,long end) throws IOException;

    void setUrl(String url);
}

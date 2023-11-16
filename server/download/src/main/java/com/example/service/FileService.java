package com.example.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
public interface FileService {
    void fileChunkDownload(String range, HttpServletRequest request, HttpServletResponse response);
}

package com.example.common;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public abstract class DownLoader implements Callable<String> {
    private String urlPath;

    private String savePath;

    private String tempPath = "L:\\temp\\";

    public boolean bpDownload = false;

    public DownLoader(String urlPath, String savePath) {
        this.urlPath = urlPath;
        this.savePath = savePath;
    }


}

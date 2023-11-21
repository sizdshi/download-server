package com.example.common;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data

public abstract class DownLoader implements Runnable {
    private String urlPath;

    private String savePath;

    private String tempPath = "M:\\";

    public boolean bpDownload = false;

    public DownLoader(String urlPath, String savePath) {
        this.urlPath = urlPath;
        this.savePath = savePath;
    }


}

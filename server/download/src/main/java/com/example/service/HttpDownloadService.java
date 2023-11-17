package com.example.service;

public interface HttpDownloadService {


    /**
     * Downloads an HTTP resource.
     *
     * @param url The URL of the HTTP resource to download.
     * @return The path of the downloaded file.
     */
    String download(String url,String savePath);
    void close();
}

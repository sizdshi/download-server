package com.example.service;

public interface HttpRequestService {


    /**
     * Downloads an HTTP resource.
     *
     * @param url The URL of the HTTP resource to download.
     * @return The path of the downloaded file.
     */
    String handleHttpRequest(String url, String savePath) throws Exception;

}

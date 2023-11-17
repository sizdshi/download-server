package com.example.service.impl;


import com.example.service.HttpDownloadService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class HttpDownloadServiceImpl implements HttpDownloadService {

    private CloseableHttpClient httpClient;

    public HttpDownloadServiceImpl() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public String download(String url, String savePath) {
        if (!url.startsWith("http")) {
            throw new IllegalArgumentException("Url should start with 'http'");
        }

        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }

        File file = null;
        try {
            // Create an HTTP GET request for the specified URL
            HttpGet httpGet = new HttpGet(url);

            // Execute the request and get the response
            HttpResponse response = httpClient.execute(httpGet);

            // Get the status code from the response
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                // If the request was successful, get the file name from the Content-Disposition header
                String disposition = response.getFirstHeader("Content-Disposition").getValue();
                String fileName = disposition.substring(disposition.lastIndexOf("=") + 2, disposition.length());

                // Save the file to the specified location
                file = new File(savePath + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(EntityUtils.toByteArray(response.getEntity()));
                fos.close();
            } else {
                System.out.println("Error: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Release resources
            if (file != null) {
                return file.getAbsolutePath();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.example.service.impl;


import com.example.service.HttpRequestService;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class HttpRequestServiceImpl implements HttpRequestService {
    public String handleHttpRequest(String url, String filePath) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(100);
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        StringBuffer response = null;
        if (responseCode == 200) { // 处理文件下载的情况
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 将相对路径转换为绝对路径
            filePath = new File(filePath).getAbsolutePath();

            // 将下载的内容保存到指定的路径
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response.toString().getBytes());
            fos.close();
        }

        return response.toString();
    }
}
package com.example.service.impl;

import com.example.Download;
import com.example.downserver.service.HttpDownload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(classes = Download.class)
@Profile("test")
public class HttpDownloadImplTest {

    @Autowired
    private HttpDownload httpDownload;

    @Test
    public void testGetFileSize() throws Exception {
        httpDownload.setUrl("https://dldir1.qq.com/qqfile/qq/QQNT/897bf087/QQ9.9.7.21484_x64.exe");
        System.out.println(httpDownload.getFileSize()+"B");
        System.out.println(httpDownload.getFileSize()/1024/1024+"MB");
    }
    @Test
    public void testReadChunk() throws Exception {
        httpDownload.setUrl("https://dldir1.qq.com/qqfile/qq/QQNT/897bf087/QQ9.9.7.21484_x64.exe");

        byte[] bytes = httpDownload.readChunk(0,100);
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(b);
        }
        String output = stringBuilder.toString();
        System.out.println(output);
        System.out.println(bytes.length);


        bytes = httpDownload.readChunk(100,500);
        StringBuilder stringBuilder2 = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder2.append(b);
        }
        output = stringBuilder2.toString();
        System.out.println(output);
        System.out.println(bytes.length);
    }

}

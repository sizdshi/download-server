package com.example.service.impl;

import com.example.Download;
import com.example.downserver.service.HttpDownload;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest(classes = Download.class)
@Profile("test")
public class HttpDownloadImplTest {

    @Autowired
    private HttpDownload httpDownload;

    /**
     * 测试获取文件大小的方法
     * 该方法无参数传入，也没有返回值，但它会验证从指定URL获取的文件大小是否正确
     * 首先设置要下载文件的URL，然后打印文件大小并验证其是否与预期值相等，
     * 最后打印文件大小以MB为单位，并验证转换后的大小是否与预期值相等
     * @throws Exception 如果在测试过程中发生错误，则抛出异常
     */
    @Test
    public void testGetFileSize() throws Exception {
        // 设置测试用的文件URL
        httpDownload.setUrl("https://dldir1.qq.com/qqfile/qq/QQNT/897bf087/QQ9.9.7.21484_x64.exe");
        // 打印文件大小（以字节为单位）
        System.out.println(httpDownload.getFileSize()+" B");
        // 验证文件大小是否与预期值185749456字节相等
        assertEquals(185749456,httpDownload.getFileSize());
        // 打印文件大小（以MB为单位）
        System.out.println(httpDownload.getFileSize()/1024/1024+" MB");
        // 验证文件大小是否与预期值177MB相等
        assertEquals(177,httpDownload.getFileSize()/1024/1024);

    }

    /**
     * 测试从指定位置读取一定长度的数据块的函数
     * 无参数
     * 无返回值，但期望通过输出和断言验证功能的正确性
     */
    @Test
    public void testReadChunk() throws Exception {
        // 设置下载对象的URL
        httpDownload.setUrl("https://dldir1.qq.com/qqfile/qq/QQNT/897bf087/QQ9.9.7.21484_x64.exe");

        // 从位置0读取100个字节，并验证读取长度
        byte[] bytes = httpDownload.readChunk(0,100);
        StringBuilder stringBuilder = new StringBuilder();
        // 将字节转换为字符串显示
        for (byte b : bytes) {
            stringBuilder.append(b);
        }
        System.out.println(stringBuilder);
        System.out.println(bytes.length);
        // 断言确认读取的字节长度是否符合预期
        assertEquals(100,bytes.length);

        // 从位置100读取500个字节，并验证读取长度
        bytes = httpDownload.readChunk(100,500);
        StringBuilder stringBuilder2 = new StringBuilder();
        // 同样将字节转换为字符串显示
        for (byte b : bytes) {
            stringBuilder2.append(b);
        }
        System.out.println(stringBuilder2);
        System.out.println(bytes.length);
        // 断言确认读取的字节长度是否符合预期
        assertEquals(400,bytes.length);
    }
}

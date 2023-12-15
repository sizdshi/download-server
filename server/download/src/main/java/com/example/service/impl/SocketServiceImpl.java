package com.example.service.impl;

import com.example.service.SocketService;

import java.io.*;
import java.net.*;
import java.util.*;

public class SocketServiceImpl implements SocketService {


    private String url;


    public SocketServiceImpl() {
        this(null);

    }

    public SocketServiceImpl(String url){
        this.setUrl(url);
    }

    @Override
    public void setUrl(String url){
        this.url = url;
    }
    @Override
    public long getFileSize() throws IOException {
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        int port = netUrl.getPort() != -1 ? netUrl.getPort() : 80;
        String path = netUrl.getPath();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 发送HTTP请求
            pw.println("HEAD " + path + " HTTP/1.1");
            pw.println("Host: " + host);
            pw.println("Accept: */*");
            pw.println("Connection: Close");
            pw.println(""); // HTTP请求头和消息体之间需要一个空行
            pw.flush();

            // 读取响应
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                if (responseLine.startsWith("Content-Length:")) {
                    long contentLength = Long.parseLong(responseLine.substring(16).trim());
                    return contentLength;
                }
            }
        }
        throw new IOException("Content-Length not found");
    }

    @Override
    public byte[] readChunk(long start, long end) throws IOException {
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        int port = netUrl.getPort() != -1 ? netUrl.getPort() : 80;
        String path = netUrl.getPath();

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port));
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            InputStream in = socket.getInputStream();

            // 发送HTTP请求
            pw.println("GET " + path + " HTTP/1.1");
            pw.println("Host: " + host);
            pw.println("Range: bytes=" + start + "-" + end);
            pw.println("Accept: */*");
            pw.println("Connection: Close");
            pw.println(""); // HTTP请求头和消息体之间需要一个空行
            pw.flush();

            // 跨过HTTP响应头
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                if (responseLine.isEmpty()) { // 当读到空行时，表明响应头部分结束
                    break;
                }
            }

            // 读取指定的字节范围
            int bufferSize = (int) (end - start + 1);
            byte[] buffer = new byte[bufferSize];
            DataInputStream dis = new DataInputStream(in);

            dis.readFully(buffer);
            return buffer;
        }
    }


}

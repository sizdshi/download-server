package com.example.service.impl;

import cn.hutool.http.HttpUtil;
import com.example.downloadercommon.common.ErrorCode;
import com.example.downloadercommon.exception.BusinessException;
import com.example.service.FileService;
import org.aspectj.util.FileUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Service
public class FileServiceImpl implements FileService {
    String fileUrl = "";
    String downloadDirectory = "";
    String fileName = "";

    /**
     * 文件分片下载
     * @param range http请求头Range，用于表示请求指定部分的内容。
     *              格式为：Range: bytes=start-end  [start,end]表示，即是包含请求头的start及end字节的内容
     * @param request
     * @param response
     */
    @Override
    public void fileChunkDownload(String range, HttpServletRequest request, HttpServletResponse response) {

        File file = new File(System.getProperty("user.dir") + "\\pom.xml");

        long startByte = 0;
        long endByte = file.length() - 1;

        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String ranges[] = range.split("-");
            try {

                if (ranges.length == 1) {
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
            }
        }
        long contentLength = endByte - startByte + 1;
        String fileName = file.getName();
        String contentType = request.getServletContext().getMimeType(fileName);
        //响应头设置
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Type", contentType);
        response.setHeader("Content-Disposition", "inline;filename=pom.xml");
        response.setHeader("Content-Length", String.valueOf(contentLength));
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(contentType);
        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            long fileSize = getFileSize(fileUrl);
            File tempFile = createTempFile(downloadDirectory,fileName,fileSize);
            int chunkSize = 1024 * 1024; // 1 MB chunks
            for (long start = 0; start < fileSize; start += chunkSize) {
                long end = Math.min(start + chunkSize, fileSize);
                downloadAndWriteChunk(fileUrl, tempFile, start, end);
            }
            File finalFile = new File(downloadDirectory, fileName);
            tempFile.renameTo(finalFile);
            System.out.println("Download completed: " + finalFile.getAbsolutePath());
//            randomAccessFile = new RandomAccessFile(file, "r");
//            outputStream = new BufferedOutputStream(response.getOutputStream());
//            byte[] buff = new byte[2048];
//            int len = 0;
//            randomAccessFile.seek(startByte);
//            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
//                outputStream.write(buff, 0, len);
//                transmitted += len;
//            }
//            if (transmitted < contentLength) {
//                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
//                outputStream.write(buff, 0, len);
//                transmitted += len;
//            }
//            outputStream.flush();
//            response.flushBuffer();
//            randomAccessFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                if (randomAccessFile != null) {
//                    randomAccessFile.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }


    private void saveToFile(String fileUrl,String downloadDirectory,String fileName){
        try {
            long fileSize = getFileSize(fileUrl);
            File tempFile = createTempFile(downloadDirectory,fileName,fileSize);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getFileSize(String fileUrl) throws IOException{

        URL url = new URL(fileUrl);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
            return url.openConnection().getContentLengthLong();
        }
//        HttpURLConnection connection = (HttpURLConnection) new URL(fileUrl).openConnection();
//        if(connection instanceof HttpURLConnection){
//            ((HttpURLConnection) connection).setRequestMethod("HEAD");
//        }
//        long contentLength = connection.getContentLengthLong();
//        return size;
    }

    private static File createTempFile(String downloadDirectory,String fileName,long fileSize) throws IOException{
        File tempFile = new File(downloadDirectory,fileName+".temp");
        try(RandomAccessFile raf = new RandomAccessFile(tempFile,"rw")){
            raf.setLength(fileSize);
            raf.seek(fileSize);
            raf.write(0);
        }
        return tempFile;
    }

    private static void downloadAndWriteChunk(String fileUrl,File tempFile,long start,long end) throws IOException{
        URL url = new URL(fileUrl);
        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock(start, end - start, false)) {
            raf.seek(start);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                raf.write(buffer, 0, bytesRead);
            }
        }
    }
}

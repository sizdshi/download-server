package com.example.manager;

import com.example.common.DownLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class SubThreadDownload extends DownLoader {

    private int threadId;
    private long startIndex;
    private long endIndex;

    private long chunkIndex;

    public SubThreadDownload(String urlPath, String savePath) {
        super(urlPath, savePath);
    }

    public void setter(int threadId, long startIndex, long endIndex) {
        this.threadId = threadId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    // 添加设置chunkIndex的方法
    public void setChunkIndex(long chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        RandomAccessFile file = null;
        try {
            //检查是否存在记录下载长度的文件，如果存在就读取这个文件的数据
//            File tempFile = new File(getTempPath() + ".temp");
//            //检查断点继续下载
//            if (tempFile.exists() && tempFile.length() > 0) {
//                FileInputStream fileInputStream = new FileInputStream(tempFile);
//                byte[] temp = new byte[1024];
//                int leng = fileInputStream.read(temp);
//                fileInputStream.close();
//                String s = new String(temp, 0, leng);
//                int dowloadlenInt = Integer.parseInt(s) - 1;
//                //修改下载的开始位置
//                startIndex += dowloadlenInt;
//
//            }

            URL url = new URL(getUrlPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(100);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {

                file = new RandomAccessFile(getSavePath()+fileName, "rwd");
                inputStream = connection.getInputStream();

                //定位文件从哪个位置开始写
                file.seek(startIndex);
                int len = 0;
                byte[] buff = new byte[1024 * 1024];
                //已经下载的数据长度
                int total = 0;

                while ((len = inputStream.read(buff)) != -1) {

                    file.write(buff, 0, len);
                    synchronized (MultipleThreadDownloadManager.class) {
                        MultipleThreadDownloadManager.progress += len;
                    }
                    total += len;
                }
                // 下载完成后，写入任务记分板文件
                writeCompletedChunkToScoreboard(chunkIndex);

            }
            System.out.println("线程：" + threadId + "号下载完毕了");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(threadId + "号线程下载错误");
        } finally {

            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeCompletedChunkToScoreboard(long chunkIndex){
        File scoreboardFile = new File(getSavePath(), "scoreboard.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreboardFile, true))) {
            writer.write(Long.toString(chunkIndex));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入任务记分板文件失败");
        }
    }
}

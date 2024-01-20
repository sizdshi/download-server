package com.example.manager;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class SubThreadDownload  {

//    private int threadId;
//    private long startIndex;
//    private long endIndex;
//    private long chunkIndex;
//
//    private  volatile boolean paused = false;
//
//
//    public SubThreadDownload(String urlPath, String savePath) {
//        super(urlPath, savePath);
//    }
//
//
//    public void setPaused(boolean paused) {
//        this.paused = paused;
//    }
//
//    public void setter(int threadId, long startIndex, long endIndex) {
//        this.threadId = threadId;
//        this.startIndex = startIndex;
//        this.endIndex = endIndex;
//    }
//
//    // 添加设置chunkIndex的方法
//    public void setChunkIndex(long chunkIndex) {
//        this.chunkIndex = chunkIndex;
//    }
//
//    @Override
//    public void run() {
//        InputStream inputStream = null;
//        RandomAccessFile file = null;
//        try {
//            URL url = new URL(getUrlPath());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(100);
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
//            String fileName = getUrlPath().substring(getUrlPath().lastIndexOf('/') + 1);
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
//
//                file = new RandomAccessFile(getSavePath()+fileName, "rwd");
//                inputStream = connection.getInputStream();
//
//                //定位文件从哪个位置开始写
//                try {
//                    synchronized (file){
//                        file.seek(startIndex);
//                        int len = 0;
//                        byte[] buff = new byte[1024 * 1024];
//                        //已经下载的数据长度
//                        int total = 0;
//                        while ((len = inputStream.read(buff)) != -1) {
//                            file.write(buff, 0, len);
//                            MultipleThreadDownloadManager.progress += len;
//                        }
//                        // 下载完成后，写入任务记分板文件
//                        writeCompletedChunkToScoreboard(chunkIndex);
//                    }
//                } finally {
//                    // 在 finally 块中释放锁，确保即使发生异常也会释放
//                    // 也可以使用 try-with-resources 语句以更好地管理资源
//                    file.close();
//                }
//
//
//            }
//            System.out.println("线程：" + threadId + "号下载完毕了");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(threadId + "号线程下载错误");
//        } finally {
//
//            if (file != null) {
//                try {
//                    file.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void writeCompletedChunkToScoreboard(long chunkIndex){
//        File scoreboardFile = new File(getSavePath(), "scoreboard.txt");
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(scoreboardFile, true))) {
//            writer.write(Long.toString(chunkIndex));
//            writer.newLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("写入任务记分板文件失败");
//        }
//    }
}

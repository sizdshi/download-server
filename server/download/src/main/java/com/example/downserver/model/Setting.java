package com.example.downserver.model;

public class Setting {
    private String storePath = "/data/downloads";
    private int maxTasks = 4;
    private int maxDownloadSpeed = 1;
    private int maxUploadSpeed = 1;

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public int getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    public int getMaxDownloadSpeed() {
        return maxDownloadSpeed;
    }

    public void setMaxDownloadSpeed(int maxDownloadSpeed) {
        this.maxDownloadSpeed = maxDownloadSpeed;
    }

    public int getMaxUploadSpeed() {
        return maxUploadSpeed;
    }

    public void setMaxUploadSpeed(int maxUploadSpeed) {
        this.maxUploadSpeed = maxUploadSpeed;
    }
}

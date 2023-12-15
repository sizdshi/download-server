package com.example.dataobject;

public class SettingDO {

    private int id;
    private String storePath;
    private int maxTasks;
    private int maxDownloadSpeed;
    private int maxUploadSpeed;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

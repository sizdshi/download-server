package com.example.downloadserver.service;

public interface SettingService {
    public String getStorePath();
    public int getMaxTasks();
    public int getMaxDownloadSpeed();
    public int getMaxUploadSpeed();
}

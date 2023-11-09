package com.example.downloadserver.service.impl;

import com.example.downloadserver.dataobject.SettingDO;
import com.example.downloadserver.mapper.SettingMapper;
import com.example.downloadserver.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;

    SettingDO settingDO = settingMapper.get();
    @Override
    public String getStorePath() {
        return settingDO.getStorePath();
    }

    @Override
    public int getMaxTasks() {
        return settingDO.getMaxTasks();
    }

    @Override
    public int getMaxDownloadSpeed() {
        return settingDO.getMaxDownloadSpeed();
    }

    @Override
    public int getMaxUploadSpeed() {
        return settingDO.getMaxUploadSpeed();
    }
}

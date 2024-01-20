package com.example.service.impl;

import com.example.dataobject.SettingDO;
import com.example.mapper.SettingMapper;
import com.example.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingMapper settingMapper;



    @Override
    public String getStorePath() {
        SettingDO settingDO = settingMapper.get();
        return settingDO.getStorePath();
    }

    @Override
    public int getMaxTasks() {
        SettingDO settingDO = settingMapper.get();
        return settingDO.getMaxTasks();
    }

    @Override
    public int getMaxDownloadSpeed() {
        SettingDO settingDO = settingMapper.get();
        return settingDO.getMaxDownloadSpeed();
    }

    @Override
    public int getMaxUploadSpeed() {
        SettingDO settingDO = settingMapper.get();
        return settingDO.getMaxUploadSpeed();
    }
}

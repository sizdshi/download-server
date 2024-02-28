package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SettingsMapper;
import com.example.model.entity.Setting;
import com.example.service.SettingsService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author sizd-shi
* @description 针对表【setting】的数据库操作Service实现
* @createDate 2024-02-18 23:23:11
*/
@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Setting>
    implements SettingsService {

    @Resource
    private SettingsMapper settingsMapper;

    @Override
    public String queryByInit() {

        Setting setting = settingsMapper.selectById(1);
        return setting.toString();
    }

    @Override
    public Setting get() {
        return null;
    }

    @Override
    public int update(Setting setting) {
        return 0;
    }

    @Override
    public String getStorePath() {
        return settingsMapper.get().getStore_path();
    }

    @Override
    public int getMaxTasks() {
        return settingsMapper.get().getMax_tasks();
    }

    @Override
    public int getMaxDownloadSpeed() {
        return settingsMapper.get().getMax_download_speed();
    }

    @Override
    public int getMaxUploadSpeed() {
        return settingsMapper.get().getMax_upload_speed();
    }

    @Override
    public Setting getSetting() {
        return settingsMapper.get();
    }

    @Override
    public int updateSetting(Setting setting) {
        return settingsMapper.update(setting);
    }
}





package com.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.entity.Setting;

/**
* @author sizd-shi
* @description 针对表【setting】的数据库操作Service
* @createDate 2024-02-18 23:23:11
*/
public interface SettingsService extends IService<Setting> {

    String queryByInit();
    Setting get();
    int update(Setting setting);
    String getStorePath();
    int getMaxTasks();
    int getMaxDownloadSpeed();
    int getMaxUploadSpeed();
    Setting getSetting();
    int updateSetting(Setting setting);
}

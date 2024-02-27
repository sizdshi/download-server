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
}





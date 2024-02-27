package com.example;

import com.example.mapper.SettingsMapper;
import com.example.model.entity.Setting;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@SpringBootTest(classes = com.example.DownloaderMain.class)
//@MybatisPlusTest
//@ContextConfiguration(classes = com.example.DownloaderMain.class)
public class SettingsTest {

    @Resource
    private SettingsMapper settingsMapper;

    @Test
    void testQuery() {
        Setting setting = settingsMapper.selectById(1);
        System.out.println(setting.toString());
    }
}

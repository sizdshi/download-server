package com.example;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.example.mapper.SettingsMapper;
import com.example.model.entity.Setting;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    void testQuery(){
        Setting setting = settingsMapper.selectById(1);
        System.out.println(setting.toString());
    }
}

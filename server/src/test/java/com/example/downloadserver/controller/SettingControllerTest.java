package com.example.downloadserver.controller;

import com.example.downloadserver.dataobject.SettingDO;
import com.example.downloadserver.mapper.SettingMapper;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SettingControllerTest {

    @Test
    public void  testGetSetting() {
        // Arrange
        SettingMapper settingMapper = mock(SettingMapper.class);
        SettingDO settingDO = new SettingDO();
        when(settingMapper.get()).thenReturn(settingDO);

        Model model = mock(Model.class);

        SettingController settingController = new SettingController();
        settingController.settingMapper = settingMapper;

        // Act
        String result = settingController.getSetting(model);

        // Assert
        verify(settingMapper, times(1)).get();
        verify(model, times(1)).addAttribute("setting", settingDO);
        assertEquals("settings", result);
    }


    @Test
    public void testPatchSetting() {
        // Arrange
        SettingMapper settingMapper = mock(SettingMapper.class);

        Model model = mock(Model.class);

        String storePath = "exampleStorePath";
        int maxTasks = 123;
        int maxDownloadSpeed = 456;
        int maxUploadSpeed = 789;

        SettingDO setting = new SettingDO();
        when(settingMapper.get()).thenReturn(setting);

        SettingController settingController = new SettingController();
        settingController.settingMapper = settingMapper;

        // Act
        String result = settingController.patchSetting(model, storePath, maxTasks, maxDownloadSpeed, maxUploadSpeed);

        // Assert
        verify(settingMapper, times(1)).get();
        verify(setting, times(1)).setStorePath(storePath);
        verify(setting, times(1)).setMaxTasks(maxTasks);
        verify(setting, times(1)).setMaxDownloadSpeed(maxDownloadSpeed);
        verify(setting, times(1)).setMaxUploadSpeed(maxUploadSpeed);
        verify(settingMapper, times(1)).update(setting);
        verify(model, times(1)).addAttribute("setting", setting);
        assertEquals("settings", result);
    }

}
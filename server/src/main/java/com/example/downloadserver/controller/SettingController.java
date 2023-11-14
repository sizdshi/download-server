package com.example.downloadserver.controller;

import com.example.downloadserver.dataobject.SettingDO;
import com.example.downloadserver.mapper.SettingMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/settings")
public class SettingController {


    @Resource
    SettingMapper settingMapper;

    SettingDO setting = new SettingDO();


    @GetMapping()
    public String getSetting(Model model) {

        setting = settingMapper.get();
        model.addAttribute("setting", setting);

        return "settings";
    }
    @PostMapping()
    public String patchSetting(Model model, @RequestParam("storePath") String storePath,
                               @RequestParam("maxTasks") int maxTasks,
                               @RequestParam("maxDownloadSpeed") int maxDownloadSpeed,
                               @RequestParam("maxUploadSpeed") int maxUploadSpeed) {
        setting.setStorePath(storePath);
        setting.setMaxTasks(maxTasks);
        setting.setMaxDownloadSpeed(maxDownloadSpeed);
        setting.setMaxUploadSpeed(maxUploadSpeed);
        settingMapper.update(setting);
        model.addAttribute("setting", setting);
        return "settings";
    }

}

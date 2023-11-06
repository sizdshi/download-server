package com.example.downloadserver.controller;

import com.example.downloadserver.model.Setting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SettingController {
    Setting setting = new Setting();
    @GetMapping("/settings")
    public String getSetting(Model model) {

        model.addAttribute(setting);

        return "settings";
    }
    @PatchMapping("/settings")
    public String patchSetting(Model model, @RequestParam("storePath") String storePath, @RequestParam("maxTasks") int maxTasks
                                    , @RequestParam("maxDownloadSpeed") int maxDownloadSpeed, @RequestParam("maxUploadSpeed") int maxUploadSpeed) {
        setting.setStorePath(storePath);
        setting.setMaxTasks(maxTasks);
        setting.setMaxDownloadSpeed(maxDownloadSpeed);
        setting.setMaxUploadSpeed(maxUploadSpeed);
        model.addAttribute("setting", setting);
        return "/settings";
    }
}

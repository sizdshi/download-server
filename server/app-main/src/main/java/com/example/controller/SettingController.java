package com.example.controller;


import com.example.model.entity.Setting;
import com.example.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private SettingsService settingsService;

    private static final Logger log = LoggerFactory.getLogger(SettingController.class);

    /**
     * 获取设置信息
     * @return ResponseEntity<Map<String, Object>> 设置信息
     */
    @GetMapping()
    public ResponseEntity<Map<String, Object>> getSetting() {
        log.info("------->getSetting");
        Setting setting = settingsService.getSetting();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("setting", setting);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //特别说明：ResponseEntity是特殊的返回对象，不需要@RequestBody注解

    /**
     * 更新设置
     *
     * @param setting 要更新的设置
     * @return 更新结果
     */
    @PostMapping()
    public ResponseEntity updateSetting(@RequestBody Setting setting){

        log.info("------->updateSetting");
        int i = settingsService.updateSetting(setting);
        return new ResponseEntity<>(i, HttpStatus.OK);
    }

}

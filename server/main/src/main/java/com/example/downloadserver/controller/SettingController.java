package com.example.downloadserver.controller;


import com.example.downloadserver.dataobject.SettingDO;
import com.example.downloadserver.mapper.SettingMapper;
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


    @Resource
    SettingMapper settingMapper;

    SettingDO setting = new SettingDO();


    @GetMapping()
    public ResponseEntity<Map<String, Object>> getSetting(Model model) {

        setting = settingMapper.get();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("setting", setting);

        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @RequestMapping()
    @ResponseBody
    public ResponseEntity updateSetting(@RequestBody SettingDO setting){
        int i = settingMapper.update(setting);
        return new ResponseEntity<>(i, HttpStatus.OK);

    }

}

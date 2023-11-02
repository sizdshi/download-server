package com.example.downloadserver.controller;

import com.example.downloadserver.model.BaseResponse;
import com.example.downloadserver.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@RestController
@Slf4j
@RequestMapping("/api")
public class TestController {

    @GetMapping("/info")
    public BaseResponse<String> queryInfo(String text){
        String result = text;
        return ResultUtils.success(result);
    }
}

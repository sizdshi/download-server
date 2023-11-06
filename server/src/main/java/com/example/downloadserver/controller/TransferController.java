package com.example.downloadserver.controller;

import com.example.downloadserver.common.ErrorCode;
import com.example.downloadserver.exception.BusinessException;
import com.example.downloadserver.model.BaseResponse;
import com.example.downloadserver.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

@RestController
@Slf4j
@RequestMapping("/task")
public class TransferController {
    @PostMapping("/thread")
    public BaseResponse<Object> changeThread(@RequestParam("id")String id, @RequestParam("num") String num, HttpServletRequest request){
        if(!StringUtils.isNotEmpty(id) || !StringUtils.isNotEmpty(num)){
            throw  new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        String result = "success";

        return ResultUtils.success(result);
    }

}

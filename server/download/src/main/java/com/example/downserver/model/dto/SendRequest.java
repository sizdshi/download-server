package com.example.downserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class SendRequest implements Serializable {

    /**
     * 存储地址
     */
    private String savePath;

    /**
     * 下载地址
     */
    private String url;

    private static final long serialVersionUID = 1L;
}

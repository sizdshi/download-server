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
     * 下载地址的数据库id
     */
    private String id;


    /**
     * 存储地址
     */
    private String savePath;

    /**
     * 下载地址
     */
    private String url;

    /**
     * 任务数量
     */
    private int threadCount;

    private static final long serialVersionUID = 1L;
}

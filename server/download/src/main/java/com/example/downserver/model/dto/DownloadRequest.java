package com.example.downserver.model.dto;


import com.example.downserver.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DownloadRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 文件名
     */
    private String file_name;

    /**
     * 下载地址
     */
    private String url;

    /**
     * 文件状态(STATUS_NONE: 默认状态 STATUS_WAITING : 等待下载 STATUS_DOWNLOADING : 正在下载
     STATUS_PAUSED : 停止下载 STATUS_DOWNLOADED : 下载完成)
     */
    private String status;

    private static final long serialVersionUID = 1L;
}

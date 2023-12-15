package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 上传下载表
 * @TableName download
 */
@TableName(value ="download")
@Data
public class Download implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 发布人
     */
    private String user_id;

    /**
     * 当前线程数
     */
    private Long count;

    /**
     * 文件状态(STATUS_NONE: 默认状态 STATUS_WAITING : 等待下载 STATUS_DOWNLOADING : 正在下载
                                                                        STATUS_PAUSED : 停止下载 STATUS_DOWNLOADED : 下载完成)
     */
    private String status;

    /**
     * 文件存储路径
     */
    private String file_url;

    /**
     * http or bt
     */
    private String task_type;

    /**
     * bt种子信息
     */
    private String torrent;

    /**
     * 曾经上传过的分片号
     */
    private Long bytes_download;

    /**
     * 文件的总分片数
     */
    private Long total_chunks;

    /**
     * 资源大小，单位为字节
     */
    private Long upload_file_size;

    /**
     * 文件MD5值
     */
    private String md5;

    /**
     * 下载时长
     */
    private String total_time;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date create_time;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date update_time;

    /**
     * 是否删除
     */

    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
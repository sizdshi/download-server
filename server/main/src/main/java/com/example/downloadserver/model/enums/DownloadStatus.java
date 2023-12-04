package com.example.downloadserver.model.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Getter
public enum DownloadStatus {
    STATUS_NONE("默认状态","STATUS_NONE"),

    STATUS_WAITING("等待下载","STATUS_WAITING"),

    STATUS_DOWNLOADING("正在下载","downloaded"),

    STATUS_PAUSED("停止下载","pending"),

    STATUS_DELETE("已删除","canceled"),

    STATUS_DOWNLOADED("下载完成","downloading");

    private final String text;

    private final String value;

    DownloadStatus(String text,String value){
        this.text = text;
        this.value = value;
    }


}

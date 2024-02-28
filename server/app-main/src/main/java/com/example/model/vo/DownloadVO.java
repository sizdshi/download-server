package com.example.model.vo;


import com.example.model.entity.Download;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class DownloadVO implements Serializable {
    /**
     * id
     */
    private String id;

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

    /**
     * 文件存储路径
     */
    private String file_url;

    /**
     * 当前线程数
     */
    private Long count;


    private static final long serialVersionUID = 1L;

    public static Download voToObj(DownloadVO downloadVO){
        if (downloadVO == null) {
            return null;
        }
        Download download = new Download();
        BeanUtils.copyProperties(downloadVO,download);
        long id = Long.parseLong(downloadVO.getId());
        download.setId(id);
        return download;
    }

    public static DownloadVO objToVo(Download download){
        if (download == null) {
            return null;
        }
        DownloadVO downloadVO = new DownloadVO();
        BeanUtils.copyProperties(download,downloadVO);
        String id = Long.toString(download.getId());
        downloadVO.setId(id);
        return  downloadVO;
    }
}

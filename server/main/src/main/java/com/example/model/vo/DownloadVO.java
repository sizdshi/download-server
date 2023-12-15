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
     * 当前线程数
     */
    private Long count;

    /**
     * 文件状态(STATUS_NONE: 默认状态 STATUS_WAITING : 等待下载 STATUS_DOWNLOADING : 正在下载
     STATUS_PAUSED : 停止下载 STATUS_DOWNLOADED : 下载完成)
     */
    private String status;



    /**
     * http or bt
     */
    private String task_type;



    /**
     * 资源大小，单位为字节
     */
    private Long upload_file_size;

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

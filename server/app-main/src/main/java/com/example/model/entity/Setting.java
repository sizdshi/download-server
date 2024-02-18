package com.example.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName setting
 */
@TableName(value ="setting")
@Data
public class Setting implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String store_path;

    /**
     * 
     */
    private Integer max_tasks;

    /**
     * 
     */
    private Integer max_download_speed;

    /**
     * 
     */
    private Integer max_upload_speed;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
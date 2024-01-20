package com.example.downserver.common;

import com.example.downserver.constant.CommonConstant;
import lombok.Data;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}

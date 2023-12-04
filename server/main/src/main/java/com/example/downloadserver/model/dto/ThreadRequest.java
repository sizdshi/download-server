package com.example.downloadserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class ThreadRequest implements Serializable {
    private String id;

    private Long count;

    private static final long serialVersionUID = 1L;
}

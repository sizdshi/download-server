package com.example.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class FilterRequest implements Serializable {
    private String status;

    private static final long serialVersionUID = 1L;
}

package com.example.downserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Kenneth shi
 * @Description:
 **/
@Data
public class SubmitRequest implements Serializable {
    private String url;

    private static final long serialVersionUID = 1L;

}

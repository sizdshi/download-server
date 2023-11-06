package com.example.downloadserver.exception;

import com.example.downloadserver.common.ErrorCode;

/**
 * @Author: Kenneth shi
 * @Description:
 **/

public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 2942420535500634982L;
    private final int code;


    public BusinessException(int code,String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 自定义错误消息
     * @param errorCode
     * @param message
     */
    public BusinessException(ErrorCode errorCode,String message){
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode(){
        return code;
    }


}

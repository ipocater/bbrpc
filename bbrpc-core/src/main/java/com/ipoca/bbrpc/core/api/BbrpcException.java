package com.ipoca.bbrpc.core.api;

import lombok.Data;

/**
 *@Author：xubang
 *@Date：2024/4/2  0:25
 */

@Data
public class BbrpcException extends RuntimeException{

    private String Errcode;

    public BbrpcException() {
    }

    public BbrpcException(String message) {
        super(message);
    }

    public BbrpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public BbrpcException(Throwable cause) {
        super(cause);
    }

    // X => 技术类异常:
    // Y => 业务类异常:
    // Z => unknown, 搞不清楚，再归类到X或Y
    public static final String SocketTimeoutEx = "X001" + "-" +"http_invoke_timeout";
    public static final String NoSuchMethodEx  = "X002" + "-" +"method_not_exists";

}

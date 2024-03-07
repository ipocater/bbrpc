package com.ipoca.bbrpc.core.api;

import lombok.Data;


@Data
public class RpcResponse<T> {

    boolean status;  // 状态: true
    T data;          // new 对象

}

package com.ipoca.bbrpc.core.api;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RpcRequest {

    private String service; // 接口: com.ipoca.bbrpc.demo.api.UserService.findById
    private String methodSign;  // 方法: findById
    private Object[] args;  // 参数: 100

}

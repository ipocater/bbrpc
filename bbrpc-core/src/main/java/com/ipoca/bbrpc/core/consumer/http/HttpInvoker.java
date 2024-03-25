package com.ipoca.bbrpc.core.consumer.http;

import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;

/**
 * @Author xubang
 * @Date 2024/3/25 17:12
 */
public interface HttpInvoker {

    RpcResponse<?> post(RpcRequest rpcRequest, String url);
}

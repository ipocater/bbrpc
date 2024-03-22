package com.ipoca.bbrpc.core.api;

import lombok.Data;

/**
 * @Author xubang
 * @Date 2024/3/22 16:29
 */
@Data
public class RpcContext {

    Filter filter; //todo

    Router router;

    LoadBalancer loadBalancer;

}

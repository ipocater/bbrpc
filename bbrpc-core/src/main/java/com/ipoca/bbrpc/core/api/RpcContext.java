package com.ipoca.bbrpc.core.api;

import com.ipoca.bbrpc.core.meta.InstanceMeta;
import lombok.Data;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 16:29
 */
@Data
public class RpcContext {

    List<Filter> filters; //todo

    Router<InstanceMeta> router;

    LoadBalancer<InstanceMeta> loadBalancer;

}

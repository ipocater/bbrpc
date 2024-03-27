package com.ipoca.bbrpc.core.filter;

import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *@Author：xubang
 *@Date：2024/3/27  23:02
 */
@Order(Integer.MIN_VALUE)
public class CacheFilter implements Filter {

    //todo 替换成guava cache,加容量和过期时间，策略
    static Map<String, Object> cache = new ConcurrentHashMap();

    @Override
    public Object preFilter(RpcRequest request) {
        return cache.get(request.toString());
    }

    @Override
    public Object postFilter(RpcRequest request, RpcResponse response, Object result) {
        cache.putIfAbsent(request.toString(), result);
        return result;
    }
}

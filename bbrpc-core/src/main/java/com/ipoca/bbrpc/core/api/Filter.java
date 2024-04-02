package com.ipoca.bbrpc.core.api;

/**
 * 过滤器.
 *
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface Filter {

    Object preFilter(RpcRequest request);

    Object postFilter(RpcRequest request, RpcResponse response, Object result);

    //Filter next();

    Filter Default = new Filter() {
        @Override
        public Object preFilter(RpcRequest request) {
            return null;
        }

        @Override
        public Object postFilter(RpcRequest request, RpcResponse response, Object result) {
            return null;
        }
    };

}

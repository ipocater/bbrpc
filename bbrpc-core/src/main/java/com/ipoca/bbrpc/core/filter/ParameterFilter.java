package com.ipoca.bbrpc.core.filter;

import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.RpcContext;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;

import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/4/12  0:26
 */
    
public class ParameterFilter implements Filter {
    @Override
    public Object preFilter(RpcRequest request) {
        Map<String, String> params = RpcContext.ContextParameters.get();
        if (!params.isEmpty()){
            request.getParams().putAll(params);
        }
        return null;
    }

    @Override
    public Object postFilter(RpcRequest request, RpcResponse response, Object result) {
        RpcContext.ContextParameters.get().clear();
        return null;
    }
}

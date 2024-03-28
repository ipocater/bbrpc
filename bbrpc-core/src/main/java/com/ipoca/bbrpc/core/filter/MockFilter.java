package com.ipoca.bbrpc.core.filter;

import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.util.MethodUtils;
import com.ipoca.bbrpc.core.util.MockUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *@Author：xubang
 *@Date：2024/3/28  22:58
 */
    
public class MockFilter implements Filter {
    @SneakyThrows
    @Override
    public Object preFilter(RpcRequest request) {
        Class service = Class.forName(request.getService());
        Method method = findMethod(service, request.getMethodSign());
        Class clazz = method.getReturnType();
        return MockUtils.mock(clazz);
    }

    private Method findMethod(Class service, String methodSign) {
        return Arrays.stream(service.getMethods())
                .filter(method -> !MethodUtils.checkLocalMethod(method))
                .filter(method -> methodSign.equals(MethodUtils.methodSing(method)))
                .findFirst().orElse(null);
    }

    @Override
    public Object postFilter(RpcRequest request, RpcResponse response, Object result) {
        return null;
    }
}

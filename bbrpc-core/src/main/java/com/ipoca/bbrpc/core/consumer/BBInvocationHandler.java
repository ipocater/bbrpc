package com.ipoca.bbrpc.core.consumer;

import com.ipoca.bbrpc.core.api.RpcException;
import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.RpcContext;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.consumer.http.HttpInvoker;
import com.ipoca.bbrpc.core.consumer.http.OkHttpInvoker;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.util.MethodUtils;
import com.ipoca.bbrpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.List;


/**
 * 消费端的动态代理处理类
 *
 *@Author：xubang
 *@Date：2024/3/15  11:10
 */
@Slf4j
public class BBInvocationHandler implements InvocationHandler {

    Class<?> service;
    RpcContext context;
    List<InstanceMeta> providers;
    HttpInvoker httpInvoker;


    public BBInvocationHandler(Class<?> clazz, RpcContext context, List<InstanceMeta> providers) {
        this.service = clazz;
        this.context = context;
        this.providers = providers;
        int timeout = Integer.parseInt(context.getParameters().getOrDefault("app.timeout", "1000"));
        httpInvoker = new OkHttpInvoker(timeout);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (MethodUtils.checkLocalMethod(method.getName())){
            return null;
        }

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethodSign(MethodUtils.methodSing(method));
        rpcRequest.setArgs(args);

        int retries = Integer.parseInt(context.getParameters().getOrDefault("app.retry","1"));
        while (retries --> 0){
            try {
                log.debug(" ===> reties: " + retries);
                //前置过滤器
                for (Filter filter : this.context.getFilters()) {
                    Object preResult = filter.preFilter(rpcRequest);
                    if (preResult != null) {
                        log.debug(filter.getClass().getName() + " ===> profilter: " + preResult);
                        return preResult;
                    }
                }

                List<InstanceMeta> instances = context.getRouter().route(providers);
                InstanceMeta instance = context.getLoadBalancer().choose(instances);
                log.debug("loadBalancer.choose(instances) ==> " + instances);

                RpcResponse<?> rpcResponse = httpInvoker.post(rpcRequest, instance.toUrl());
                Object result = castReturnResult(method, rpcResponse);
                //方法调用后置过滤器
                for (Filter filter : this.context.getFilters()) {
                    Object filterResult = filter.postFilter(rpcRequest, rpcResponse, result);
                    if (filterResult != null) {
                        return filterResult;
                    }
                }
                return result;
            } catch (RuntimeException ex){
                if (!(ex.getCause() instanceof SocketTimeoutException)){
                    throw ex;
                }
            }
        }
        return null;
    }

    private static Object castReturnResult(Method method, RpcResponse<?> rpcResponse) {
        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            return TypeUtils.castMethodResult(method, data);
        } else {
            Exception exception = rpcResponse.getEx();
            if (exception instanceof RpcException ex){
                throw ex;
            } else {
                throw new RpcException(rpcResponse.getEx(), RpcException.UnknownEx);
            }
        }
    }

}

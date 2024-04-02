package com.ipoca.bbrpc.core.consumer;

import com.ipoca.bbrpc.core.api.RpcException;
import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.RpcContext;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.consumer.http.HttpInvoker;
import com.ipoca.bbrpc.core.consumer.http.OkHttpInvoker;
import com.ipoca.bbrpc.core.governance.SlidingTimeWindow;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.util.MethodUtils;
import com.ipoca.bbrpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    final List<InstanceMeta> providers;

    final List<InstanceMeta> isolatedProviders = new ArrayList<>();


    final List<InstanceMeta> halfOpenProviders = new ArrayList<>();

    Map<String, SlidingTimeWindow> windows = new HashMap<>();

    HttpInvoker httpInvoker;

    ScheduledExecutorService executor;


    public BBInvocationHandler(Class<?> clazz, RpcContext context, List<InstanceMeta> providers) {
        this.service = clazz;
        this.context = context;
        this.providers = providers;
        int timeout = Integer.parseInt(context.getParameters().getOrDefault("app.timeout", "1000"));
        httpInvoker = new OkHttpInvoker(timeout);
        this.executor = Executors.newScheduledThreadPool(1);
        this.executor.scheduleWithFixedDelay(this::halfOpen, 10, 60, TimeUnit.SECONDS);
    }

    private void halfOpen() {
        log.debug(" ====> half open isolatedProviders: " + isolatedProviders);
        halfOpenProviders.clear();
        halfOpenProviders.addAll(isolatedProviders);
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

                for (Filter filter : this.context.getFilters()) {
                    Object preResult = filter.preFilter(rpcRequest);
                    if (preResult != null) {
                        log.debug(filter.getClass().getName() + " ===> profilter: " + preResult);
                        return preResult;
                    }
                }

                InstanceMeta instance;
                synchronized (halfOpenProviders) {
                    if (halfOpenProviders.isEmpty()) {
                        List<InstanceMeta> instances = context.getRouter().route(providers);
                        instance = context.getLoadBalancer().choose(instances);
                        log.debug("loadBalancer.choose(instances) ==> " + instances);
                    } else {
                        instance = halfOpenProviders.remove(0);
                        log.debug(" check alive instance ==> {}", instance);
                    }
                }

                RpcResponse<?> rpcResponse;
                Object result;

                String url = instance.toUrl();
                try {
                    rpcResponse = httpInvoker.post(rpcRequest, url);
                    result = castReturnResult(method, rpcResponse);
                } catch (Exception e){
                    // 故障的规则统计和隔离
                    // 每一次异常，记录一次，统计30s的异常数


                    SlidingTimeWindow window = windows.get(url);
                    if (window == null){
                        window = new SlidingTimeWindow();
                        windows.put(url, window);
                    }

                    window.record(System.currentTimeMillis());
                    log.debug("instance {} in window with {}", url, window.getSum());
                    // 发生了10次，就做故障隔离
                    if(window.getSum() >= 10){
                        isolate(instance);
                    }
                    throw e;
                }

                //判断是否探活成功
                synchronized (providers) {
                    if (!providers.contains(instance)) {
                        isolatedProviders.remove(instance);
                        providers.add(instance);
                        log.debug("instance {} is recovered, isolatedProviders={}, providers={}", instance, isolatedProviders, providers);
                    }
                }
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

    private void isolate(InstanceMeta instance) {

        log.debug(" ==> isolate instance: " + instance);
        providers.remove(instance);
        log.debug(" ==> providers =  {}", providers);
        isolatedProviders.add(instance);
        log.debug(" ==> isolatedProviders =  {}", providers);

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

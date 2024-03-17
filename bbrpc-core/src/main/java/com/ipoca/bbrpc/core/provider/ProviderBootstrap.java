package com.ipoca.bbrpc.core.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/3/13  21:47
 */
@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private Map<String, Object> skeleton = new HashMap<>();

    @PostConstruct // init-method
    // PreDestroy
    public void buildProviders() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(BBProvider.class);
        providers.forEach((x,y) -> System.out.println(x));

        providers.values().forEach(
                x -> genInterface(x)
        );
    }

    private void genInterface(Object x) {
        Class<?> itfer = x.getClass().getInterfaces()[0];
        skeleton.put(itfer.getCanonicalName(), x);
    }

    public RpcResponse invokeRequest(RpcRequest request) {

        String methodName= request.getMethod();
        if (methodName.equals("toString") || methodName.equals("hashCode")){
            return null;
        }

        RpcResponse rpcResponse = new RpcResponse();
        Object bean = skeleton.get(request.getService());
        try {
            Method method = findMethod(bean.getClass(),request.getMethod());
            Object result = method.invoke(bean, request.getArgs());
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
            return rpcResponse;
        } catch (InvocationTargetException e) {
            rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        return rpcResponse;
    }

    private Method findMethod(Class<?> aClass, String methodName) {
        for (Method method : aClass.getMethods()) {
            if (method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }

}

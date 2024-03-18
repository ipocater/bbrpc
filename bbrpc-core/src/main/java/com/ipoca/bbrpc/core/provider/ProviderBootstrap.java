package com.ipoca.bbrpc.core.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.meta.ProviderMeta;
import com.ipoca.bbrpc.core.util.MethodUtils;
import com.ipoca.bbrpc.core.util.TypeUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *@Author：xubang
 *@Date：2024/3/13  21:47
 */
@Data
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;

    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

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
        Arrays.stream(x.getClass().getInterfaces()).forEach(
                itfer -> {
                    Method[] methods = itfer.getMethods();
                    for (Method method : methods){
                        if (MethodUtils.checkLocalMethod(method)){
                            continue;
                        }
                        createProvider(itfer, x, method);
                    }
                }
        );
    }

    private void createProvider(Class<?> itfer, Object x, Method method) {
        ProviderMeta meta = new ProviderMeta();
        meta.setMethod(method);
        meta.setServiceImpl(x);
        meta.setMethodSign(MethodUtils.methodSing(method));
        System.out.println(" create a provider: " +meta);
        skeleton.add(itfer.getCanonicalName(), meta);
    }

    public RpcResponse invoke(RpcRequest request) {
        RpcResponse rpcResponse = new RpcResponse();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            ProviderMeta meta = findProviderMeta(providerMetas,request.getMethodSign());

            Method method = meta.getMethod();
            Object[] args = processArgs(request.getArgs(), method.getParameterTypes());
            Object result = method.invoke(meta.getServiceImpl(), args);
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

    private Object[] processArgs(Object[] args, Class<?>[] parameterTypes) {
        if (args == null || args.length == 0){
            return args;
        }

        Object[] actuals = new Object[args.length];
        for (int i = 0; i < args.length; i++){
            actuals[i] = TypeUtils.cast(args[i], parameterTypes[i]);
        }
        return actuals;

    }

    private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
        Optional<ProviderMeta> optional = providerMetas.stream()
                .filter(x->x.getMethodSign().equals(methodSign)).findFirst();
        return optional.orElse(null);
    }

}

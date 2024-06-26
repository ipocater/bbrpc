package com.ipoca.bbrpc.core.consumer;

import com.ipoca.bbrpc.core.annotation.BBConsumer;
import com.ipoca.bbrpc.core.api.*;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.meta.ServiceMeta;
import com.ipoca.bbrpc.core.util.MethodUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/3/15  10:53
 */
    
@Data
@Slf4j
public class ConsumerBootstrap  implements ApplicationContextAware, EnvironmentAware {

    ApplicationContext applicationContext;
    Environment environment;

    @Value("${app.id}")
    private String app;

    @Value("${app.namespace}")
    private String namespace;

    @Value("${app.env}")
    private String env;

    @Value("${app.retry}")
    private int retry;

    @Value("${app.timeout}")
    private int timeout;

    @Value("${app.grayRatio}")
    private int grayRatio;

    private Map<String, Object> stub = new HashMap<>();
    
    public void start() {
        Router<InstanceMeta> router = applicationContext.getBean(Router.class);
        LoadBalancer<InstanceMeta> loadBalancer = applicationContext.getBean(LoadBalancer.class);
        RegistryCenter rc = applicationContext.getBean(RegistryCenter.class);
        List<Filter> filters = applicationContext.getBeansOfType(Filter.class).values().stream().toList();

        RpcContext context = new RpcContext();
        context.setRouter(router);
        context.setLoadBalancer(loadBalancer);
        context.setFilters(filters);
        context.getParameters().put("app.retry",String.valueOf(retry));
        context.getParameters().put("app.timeout",String.valueOf(timeout));
        //context.getParameters().put("app.grayRatio",String.valueOf(grayRatio));

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names){
            Object bean = applicationContext.getBean(name);
            List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass(), BBConsumer.class);

            fields.stream().forEach( f ->{
                log.info(" ===>  " + f.getName());
                try {
                    Class<?> service = f.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null){
                        consumer = createFromRegisry(service, context, rc);
                        stub.put(serviceName, consumer);
                    }
                    f.setAccessible(true);
                    f.set(bean, consumer);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            });
        }
    }

    private Object createFromRegisry(Class<?> service, RpcContext context, RegistryCenter rc) {
        String serviceName = service.getCanonicalName();
        ServiceMeta serviceMeta = ServiceMeta.builder().app(app).env(env).namespace(namespace).name(serviceName).build();
        List<InstanceMeta> providers = rc.fetchAll(serviceMeta);
        log.info(" ===> map to providers: ");
        providers.forEach(System.out::println);

        rc.subscribe(serviceMeta, event -> {
            providers.clear();
            providers.addAll(event.getData());
        });
        return createConsumer(service, context, providers);
    }

    private Object createConsumer(Class<?> service, RpcContext context, List<InstanceMeta> providers) {
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new BBInvocationHandler(service, context, providers));
    }

}

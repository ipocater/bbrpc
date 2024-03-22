package com.ipoca.bbrpc.core.consumer;

import com.ipoca.bbrpc.core.annotation.BBConsumer;
import com.ipoca.bbrpc.core.api.LoadBalancer;
import com.ipoca.bbrpc.core.api.Router;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/3/15  10:53
 */
    
@Data
public class ConsumerBootstrap  implements ApplicationContextAware, EnvironmentAware {

    ApplicationContext applicationContext;
    Environment environment;

    private Map<String, Object> stub = new HashMap<>();
    
    public void start() {

        Router router = applicationContext.getBean(Router.class);
        LoadBalancer loadBalancer = applicationContext.getBean(LoadBalancer.class);

        String urls = environment.getProperty("bbrpc.providers", "");
        if (Strings.isEmpty(urls)){
            System.out.println("bbrpc.providers is empty.");
        }
        String[] providers = urls.split(",");

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names){
            Object bean = applicationContext.getBean(name);
            List<Field> fields = findAnnotatedField(bean.getClass());

            fields.stream().forEach( f ->{
                System.out.println(" ===>  " + f.getName());
                try {
                    Class<?> service = f.getType();
                    String serviceName = service.getCanonicalName();
                    Object consumer = stub.get(serviceName);
                    if (consumer == null){
                        consumer = createConsumer(service, router, loadBalancer, providers);
                    }
                    f.setAccessible(true);
                    f.set(bean, consumer);
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            });
        }
    }

    private Object createConsumer(Class<?> service, Router router, LoadBalancer loadBalancer, String[] providers) {
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new BBInvocationHandler(service, router, loadBalancer, providers));

    }

    //todo  stream重写
    private List<Field> findAnnotatedField(Class<?> aClass) {
        List<Field> result = new ArrayList<>();
        while (aClass != null){
            Field[] fields = aClass.getDeclaredFields();
            for (Field field :fields){
                if (field.isAnnotationPresent(BBConsumer.class)){
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }


}

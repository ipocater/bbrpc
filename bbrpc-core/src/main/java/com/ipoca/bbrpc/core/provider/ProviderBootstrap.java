package com.ipoca.bbrpc.core.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.config.AppConfigProperties;
import com.ipoca.bbrpc.core.config.ProviderConfigProperties;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.meta.ProviderMeta;
import com.ipoca.bbrpc.core.meta.ServiceMeta;
import com.ipoca.bbrpc.core.util.MethodUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/3/13  21:47
 */
@Data
@Slf4j
public class ProviderBootstrap implements ApplicationContextAware {

    ApplicationContext applicationContext;
    RegistryCenter rc;
    private String port;
    private AppConfigProperties appProperties;
    private ProviderConfigProperties providerProperties;
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();
    private InstanceMeta instance;

    public ProviderBootstrap(String port, AppConfigProperties appProperties,
                             ProviderConfigProperties providerProperties){
        this.port = port;
        this.appProperties = appProperties;
        this.providerProperties = providerProperties;
    }

    @SneakyThrows
    @PostConstruct // init-method
    public void init() {
        Map<String, Object> providers = applicationContext.getBeansWithAnnotation(BBProvider.class);
        rc  = applicationContext.getBean(RegistryCenter.class);
        providers.keySet().forEach(System.out::println);
        providers.values().forEach(this::genInterface);
    }
    @SneakyThrows
    public void start(){
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = InstanceMeta.http(ip, Integer.valueOf(port)).addParams(providerProperties.getMetas());
        rc.start();
        skeleton.keySet().forEach(this::registerService);
    }
    @PreDestroy
    public void stop(){
        skeleton.keySet().forEach(this::unregisterService);
        rc.stop();
    }

    private void registerService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(appProperties.getId())
                .env(appProperties.getEnv())
                .namespace(appProperties.getNamespace())
                .name(service)
                .build();
        rc.register(serviceMeta, instance);
    }

    private void unregisterService(String service) {
        ServiceMeta serviceMeta = ServiceMeta.builder()
                .app(appProperties.getId())
                .env(appProperties.getEnv())
                .namespace(appProperties.getNamespace())
                .name(service)
                .build();
        rc.unregister(serviceMeta, instance);
    }

    private void genInterface(Object impl) {
        Arrays.stream(impl.getClass().getInterfaces()).forEach(
                service -> {
                    Method[] methods = service.getMethods();
                    for (Method method : methods){
                        if (MethodUtils.checkLocalMethod(method)) continue;
                        createProvider(service, impl, method);
                    }
                }
        );
    }
    private void createProvider(Class<?> service, Object impl, Method method) {
        ProviderMeta pro = ProviderMeta.builder()
                .serviceImpl(impl).method(method).methodSign(MethodUtils.methodSing(method)).build();
        log.info(" create a provider: " +pro);
        skeleton.add(service.getCanonicalName(), pro);
    }
}

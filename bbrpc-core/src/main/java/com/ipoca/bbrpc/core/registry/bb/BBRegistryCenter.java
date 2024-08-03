package com.ipoca.bbrpc.core.registry.bb;

import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.meta.ServiceMeta;
import com.ipoca.bbrpc.core.registry.ChangedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 *@Author：xubang
 *@Date：2024/8/3  21:34
 */

@Slf4j
public class BBRegistryCenter implements RegistryCenter {

    @Value("${bbregistry.servers}")
    private String servers;


    @Override
    public void start() {
        log.info(" ====>>>> [BBRegistry] : start with server : {}", servers);
    }

    @Override
    public void stop() {
        log.info(" ====>>>> [BBRegistry] : stop with server : {}", servers);
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        log.info(" ====>>>> [BBRegistry] : registry instance {} for {}", instance, service);

    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {

    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        return null;
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {

    }
}

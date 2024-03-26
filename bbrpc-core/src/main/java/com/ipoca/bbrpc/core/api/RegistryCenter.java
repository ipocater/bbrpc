package com.ipoca.bbrpc.core.api;

import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.meta.ServiceMeta;
import com.ipoca.bbrpc.core.registry.ChangedListener;
import com.ipoca.bbrpc.core.registry.Event;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 16:57
 */
public interface RegistryCenter {

    void start();
    void stop();

    // provider侧
    void register(ServiceMeta service, InstanceMeta instance);
    void unregister(ServiceMeta service, InstanceMeta instance);

    // consumer侧
    List<InstanceMeta> fetchAll(ServiceMeta service);
    void subscribe(ServiceMeta service, ChangedListener listener);

    class StaticRegistryCenter implements RegistryCenter,ChangedListener{

        List<InstanceMeta> providers;
        public StaticRegistryCenter(List<InstanceMeta> providers){
            this.providers = providers;
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void register(ServiceMeta service, InstanceMeta instance) {

        }

        @Override
        public void unregister(ServiceMeta service, InstanceMeta instance) {

        }

        @Override
        public List<InstanceMeta> fetchAll(ServiceMeta service) {
            return providers;
        }

        @Override
        public void subscribe(ServiceMeta service, ChangedListener listener) {

        }

        @Override
        public void fire(Event event) {

        }
    }

}

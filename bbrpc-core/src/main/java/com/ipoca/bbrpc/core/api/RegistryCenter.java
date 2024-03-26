package com.ipoca.bbrpc.core.api;

import com.ipoca.bbrpc.core.meta.InstanceMeta;
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
    void register(String service, InstanceMeta instance);
    void unregister(String service, InstanceMeta instance);

    // consumer侧
    List<InstanceMeta> fetchAll(String service);
    void subscribe(String service, ChangedListener listener);

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
        public void register(String service, InstanceMeta instance) {

        }

        @Override
        public void unregister(String service, InstanceMeta instance) {

        }

        @Override
        public List<InstanceMeta> fetchAll(String service) {
            return providers;
        }

        @Override
        public void subscribe(String service, ChangedListener listener) {

        }

        @Override
        public void fire(Event event) {

        }
    }

}

package com.ipoca.bbrpc.core.api;

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
    void register(String service, String instance);
    void unregister(String service, String instance);

    // consumer侧
    List<String> fetchAll(String service);
    void subscribe(String service, ChangedListener listener);

    class StaticRegistryCenter implements RegistryCenter,ChangedListener{

        List<String> providers;
        public StaticRegistryCenter(List<String> providers){
            this.providers = providers;
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void register(String service, String instance) {

        }

        @Override
        public void unregister(String service, String instance) {

        }

        @Override
        public List<String> fetchAll(String service) {
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

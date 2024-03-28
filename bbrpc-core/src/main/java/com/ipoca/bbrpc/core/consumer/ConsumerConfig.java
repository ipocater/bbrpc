package com.ipoca.bbrpc.core.consumer;
/**
 *@Author：xubang
 *@Date：2024/3/15  10:55
 */

import com.ipoca.bbrpc.core.api.Filter;
import com.ipoca.bbrpc.core.api.LoadBalancer;
import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.api.Router;
import com.ipoca.bbrpc.core.cluster.RoundRibonLoadBalancer;
import com.ipoca.bbrpc.core.filter.CacheFilter;
import com.ipoca.bbrpc.core.filter.MockFilter;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.registry.zk.ZkRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
public class ConsumerConfig {
    @Value("${bbrpc.providers}")
    String servers;

    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner consumerBootstrap_runner(@Autowired ConsumerBootstrap consumerBootstrap) {
        return x -> {
            log.info("consumerBootstrap starting ...");
            consumerBootstrap.start();
            log.info("consumerBootstrap started ...");
        };
    }

    @Bean
    public LoadBalancer<InstanceMeta> loadBalancer(){
        //return new RandomLoadBalancer();
        return new RoundRibonLoadBalancer();
    }

    @Bean
    public Router<InstanceMeta> router(){
        return Router.Default;
    }

    @Bean
    public Filter filter1(){
        return new CacheFilter();
    }

//    @Bean
//    public Filter filter2(){
//        return new MockFilter();
//    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter consumer_rc(){
        return new ZkRegistryCenter();
    }
}

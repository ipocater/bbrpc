package com.ipoca.bbrpc.core.config;
/**
 *@Author：xubang
 *@Date：2024/3/15  10:55
 */

import com.ipoca.bbrpc.core.api.*;
import com.ipoca.bbrpc.core.cluster.GrayRouter;
import com.ipoca.bbrpc.core.cluster.RoundRibonLoadBalancer;
import com.ipoca.bbrpc.core.consumer.ConsumerBootstrap;
import com.ipoca.bbrpc.core.filter.CacheFilter;
import com.ipoca.bbrpc.core.filter.MockFilter;
import com.ipoca.bbrpc.core.filter.ParameterFilter;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.registry.zk.ZkRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@Slf4j
@Import({AppConfigProperties.class, ConsumerConfig.class})
public class ConsumerConfig {

    @Autowired
    AppConfigProperties appConfigProperties;

    @Autowired
    ConsumerConfigProperties consumerConfigProperties;

    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        return new ConsumerBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE+1)
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
        return new GrayRouter(consumerConfigProperties.getGrayRatio());
    }

    @Bean
    public Filter defaultFilter(){
        return new ParameterFilter();
    }

//    @Bean
//    public Filter filter1(){
//        return new CacheFilter();
//    }

//    @Bean
//    public Filter filter2(){
//        return new MockFilter();
//    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter consumer_rc(){
        return new ZkRegistryCenter();
    }

    @Bean
    public RpcContext createContext(@Autowired Router router,
                                    @Autowired LoadBalancer loadBalancer,
                                    @Autowired List<Filter> filters) {
        RpcContext context = new RpcContext();
        context.setRouter(router);
        context.setLoadBalancer(loadBalancer);
        context.setFilters(filters);
        context.getParameters().put("app.id", appConfigProperties.getId());
        context.getParameters().put("app.namespace", appConfigProperties.getNamespace());
        context.getParameters().put("app.env", appConfigProperties.getEnv());
        context.getParameters().put("consumer.retries", String.valueOf(consumerConfigProperties.getRetries()));
        context.getParameters().put("consumer.timeout", String.valueOf(consumerConfigProperties.getTimeout()));
        context.getParameters().put("consumer.faultLimit", String.valueOf(consumerConfigProperties.getFaultLimit()));
        context.getParameters().put("consumer.halfOpenInitialDelay", String.valueOf(consumerConfigProperties.getHalfOpenDelay()));
        context.getParameters().put("consumer.halfOpenDelay", String.valueOf(consumerConfigProperties.getHalfOpenDelay()));
        return context;
    }
}

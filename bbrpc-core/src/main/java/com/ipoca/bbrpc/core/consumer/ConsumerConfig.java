package com.ipoca.bbrpc.core.consumer;
/**
 *@Author：xubang
 *@Date：2024/3/15  10:55
 */

import com.ipoca.bbrpc.core.api.LoadBalancer;
import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.api.Router;
import com.ipoca.bbrpc.core.cluster.RandomLoadBalancer;
import com.ipoca.bbrpc.core.cluster.RoundRibonLoadBalancer;
import com.ipoca.bbrpc.core.registry.ZkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
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
            System.out.println("consumerBootstrap starting ...");
            consumerBootstrap.start();
            System.out.println("consumerBootstrap started ...");
        };
    }

    @Bean
    public LoadBalancer loadBalancer(){
        //return new RandomLoadBalancer();
        return new RoundRibonLoadBalancer();
    }

    @Bean
    public Router router(){
        return Router.Default;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter consumer_rc(){
        return new ZkRegistryCenter();
    }
}

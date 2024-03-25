package com.ipoca.bbrpc.core.provider;

import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.consumer.ConsumerBootstrap;
import com.ipoca.bbrpc.core.registry.ZkRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 *@Author：xubang
 *@Date：2024/3/13  21:50
 */

@Configuration
public class ProviderConfig {

    @Bean
    ProviderBootstrap providerBootstrap(){
        return new ProviderBootstrap();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            System.out.println("consumerBootstrap starting ...");
            providerBootstrap.start();
            System.out.println("consumerBootstrap started ...");
        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter provider_rc(){
        return new ZkRegistryCenter();
    }
}

package com.ipoca.bbrpc.core.config;

import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.provider.ProviderBootstrap;
import com.ipoca.bbrpc.core.provider.ProviderInvoker;
import com.ipoca.bbrpc.core.registry.zk.ZkRegistryCenter;
import com.ipoca.bbrpc.core.transport.SpringBootTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 *@Author：xubang
 *@Date：2024/3/13  21:50
 */

@Configuration
@Slf4j
@Import({AppConfigProperties.class, ProviderConfig.class,SpringBootTransport.class})
public class ProviderConfig {

    @Value("${server.port:8080}")
    private String port;

    @Autowired
    AppConfigProperties appConfigProperties;

    @Autowired
    ProviderConfigProperties providerConfigProperties;

    @Bean
    ProviderBootstrap providerBootstrap(){
        return new ProviderBootstrap(port, appConfigProperties, providerConfigProperties);
    }

    @Bean
    ProviderInvoker providerInvoker(@Autowired ProviderBootstrap providerBootstrap) {
        return new ProviderInvoker(providerBootstrap);
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrap_runner(@Autowired ProviderBootstrap providerBootstrap) {
        return x -> {
            log.info("providerBootstrap starting ...");
            providerBootstrap.start();
            log.info("providerBootstrap started ...");
        };
    }

    @Bean//(initMethod = "start", destroyMethod = "stop")
    public RegistryCenter provider_rc(){
        return new ZkRegistryCenter();
    }
}

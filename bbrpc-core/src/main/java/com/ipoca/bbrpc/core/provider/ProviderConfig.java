package com.ipoca.bbrpc.core.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}

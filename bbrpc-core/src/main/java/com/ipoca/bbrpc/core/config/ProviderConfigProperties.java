package com.ipoca.bbrpc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *@Author：xubang
 *@Date：2024/4/28  21:07
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "bbrpc.provider")
public class ProviderConfigProperties {

    Map<String, String> metas = new HashMap<>();
}

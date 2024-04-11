package com.ipoca.bbrpc.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *@Author：xubang
 *@Date：2024/4/12  0:53
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "bbrpc.app")
public class AppConfigProperties {

    private String id = "app1";

    private String namespace = "public";

    private String env = "dev";
}

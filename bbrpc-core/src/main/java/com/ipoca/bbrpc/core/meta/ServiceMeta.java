package com.ipoca.bbrpc.core.meta;

import lombok.Builder;
import lombok.Data;

/**
 * 描述provider的映射关系
 *
 * @Author xubang
 * @Date 2024/3/26 10:47
 */
@Data
@Builder
public class ServiceMeta {

    private String app;
    private String namespace;
    private String env;
    private String name;

    public String toPath() {
        return String.format("%s_%s_%s_%s", app, namespace, env, name);
    }
}

package com.ipoca.bbrpc.core.api;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface LoadBalancer {

    String choose(List<String> providers);

    LoadBalancer Default = p -> p == null || p.size() == 0 ? null : p.get(0);
}

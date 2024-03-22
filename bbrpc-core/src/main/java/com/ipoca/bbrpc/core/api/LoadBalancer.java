package com.ipoca.bbrpc.core.api;

import java.util.List;

/**
 * 负载均衡, weightedRR,AAWR-自适应,
 *
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface LoadBalancer<T> {

    T choose(List<T> providers);

    LoadBalancer Default = p -> p == null || p.size() == 0 ? null : p.get(0);
}

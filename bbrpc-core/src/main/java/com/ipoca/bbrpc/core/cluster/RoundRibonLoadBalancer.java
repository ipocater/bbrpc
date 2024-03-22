package com.ipoca.bbrpc.core.cluster;

import com.ipoca.bbrpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xubang
 * @Date 2024/3/22 15:05
 */
public class RoundRibonLoadBalancer implements LoadBalancer {

    AtomicInteger index = new AtomicInteger(0);
    @Override
    public String choose(List<String> providers) {
        if (providers == null || providers.size() ==0) return null;
        if (providers.size() == 1) return providers.get(0);
        return providers.get(index.getAndIncrement() % providers.size());
    }
}

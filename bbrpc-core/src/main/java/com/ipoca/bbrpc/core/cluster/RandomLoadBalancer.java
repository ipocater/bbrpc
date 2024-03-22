package com.ipoca.bbrpc.core.cluster;

import com.ipoca.bbrpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * @Author xubang
 * @Date 2024/3/22 15:05
 */
public class RandomLoadBalancer implements LoadBalancer {

    Random random = new Random();
    @Override
    public String choose(List<String> providers) {
        if (providers == null || providers.size() ==0) return null;
        if (providers.size() == 1) return providers.get(0);
        return providers.get(random.nextInt(providers.size()));
    }
}

package com.ipoca.bbrpc.core.api;

import com.ipoca.bbrpc.core.meta.InstanceMeta;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface Router<T> {

    List<T> route(List<T> providers);

    Router<InstanceMeta> Default = p -> p;

}

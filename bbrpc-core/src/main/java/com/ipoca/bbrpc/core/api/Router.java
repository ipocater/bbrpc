package com.ipoca.bbrpc.core.api;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface Router<T> {

    List<T> route(List<T> providers);

    Router Default = p -> p;

}

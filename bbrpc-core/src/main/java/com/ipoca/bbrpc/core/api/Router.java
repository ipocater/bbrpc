package com.ipoca.bbrpc.core.api;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 11:16
 */
public interface Router {

    List<String> route(List<String> providers);

    Router Default = p -> p;

}

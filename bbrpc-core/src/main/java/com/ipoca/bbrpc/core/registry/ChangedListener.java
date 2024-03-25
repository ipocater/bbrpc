package com.ipoca.bbrpc.core.registry;

/**
 * @Author xubang
 * @Date 2024/3/25 14:38
 */
public interface ChangedListener {
    void fire(Event event);
}

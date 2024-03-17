package com.ipoca.bbrpc.core.meta;

import lombok.Data;

import java.lang.reflect.Method;

/**
 *@Author：xubang
 *@Date：2024/3/17  19:01
 */

@Data
public class ProviderMeta {

    Method method;
    String methodSign;
    Object serviceImpl;

}

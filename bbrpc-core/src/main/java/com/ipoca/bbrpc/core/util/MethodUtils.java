package com.ipoca.bbrpc.core.util;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *@Author：xubang
 *@Date：2024/3/17  16:40
 */
    
public class MethodUtils {

    public static boolean checkLocalMethod(final String method){
        //本地方法不代理
        if ("toString".equals(method)      ||
                "hashCode".equals(method)  ||
                "notifyAll".equals(method) ||
                "equals".equals(method)    ||
                "wait".equals(method)      ||
                "getClass".equals(method)  ||
                "notify".equals(method)){
            return true;
        }
        return false;
    }

    public static boolean checkLocalMethod(final Method method){
        return method.getDeclaringClass().equals(Object.class);
    }

    public static String methodSing(Method method){
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append("@").append(method.getParameterCount());
        Arrays.stream(method.getParameterTypes()).forEach(
                c -> sb.append("_").append(c.getCanonicalName())
        );
        return sb.toString();
    }

    public static void main(String[] args) {
        Arrays.stream(MethodUtils.class.getMethods()).forEach(
                m -> System.out.println(methodSing(m))
        );

    }
}

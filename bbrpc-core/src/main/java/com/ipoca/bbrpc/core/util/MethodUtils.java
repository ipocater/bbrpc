package com.ipoca.bbrpc.core.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *@Author：xubang
 *@Date：2024/3/17  16:40
 */

@Slf4j
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
                m -> log.debug(methodSing(m))
        );
    }

    public static List<Field> findAnnotatedField(Class<?> aClass, Class<? extends Annotation> annotationClass) {
        List<Field> result = new ArrayList<>();
        while (aClass != null){
            Field[] fields = aClass.getDeclaredFields();
            for (Field field :fields){
                if (field.isAnnotationPresent(annotationClass)){
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }
}

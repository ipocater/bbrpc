package com.ipoca.bbrpc.core.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型转换工具类
 *
 *@Author：xubang
 *@Date：2024/3/18  20:49
 */

@Slf4j
public class TypeUtils {
    public static Object cast(Object origin, Class<?> type) {
        log.debug("cast: origin = " + origin);
        log.debug("cast: type = " + type);
        if (origin == null) return null;
        Class<?> aClass = origin.getClass();
        if (type.isAssignableFrom(aClass)){
            return origin;
        }

        if (type.isArray()){
            Object[] arr;
            if (origin instanceof List list){
                origin = list.toArray();
            }
            int length = Array.getLength(origin);
            Class<?> componentType = type.getComponentType();
            Object resultArray = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++){
                Array.set(resultArray, i, Array.get(origin, i));
            }
            return resultArray;
        }

        if (origin instanceof HashMap map){
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toJavaObject(type);
        }

        if (origin instanceof JSONObject jsonObject) {
            return jsonObject.toJavaObject(type);
        }

        if (type.equals(Integer.class) || type.equals(Integer.TYPE)){
            return Integer.valueOf(origin.toString());
        }else if (type.equals(Long.class) || type.equals(Long.TYPE)){
            return Long.valueOf(origin.toString());
        }else if (type.equals(Float.class) || type.equals(Float.TYPE)){
            return Float.valueOf(origin.toString());
        }else if (type.equals(Double.class) || type.equals(Double.TYPE)){
            return Double.valueOf(origin.toString());
        }else if (type.equals(Short.class) || type.equals(Short.TYPE)){
            return Short.valueOf(origin.toString());
        }else if (type.equals(Character.class) || type.equals(Character.TYPE)){
            return Character.valueOf(origin.toString().charAt(0));
        }else if (type.equals(Byte.class) || type.equals(Byte.TYPE)){
            return Byte.valueOf(origin.toString());
        }else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)){
            return Boolean.valueOf(origin.toString());
        }
        return null;
    }

    @Nullable
    public static Object castMethodResult(Method method, Object data) {
        log.debug("castMethodResult: method = " + method);
        log.debug("castMethodResult: data = " + data);
        Class<?> type = method.getReturnType();
        Type genericReturnType = method.getGenericReturnType();
        return castGeneric(data, type, genericReturnType);
    }

    public static Object castGeneric(Object data, Class<?> type, Type genericReturnType) {
        log.debug("castGeneric: data = " + data);
        log.debug("castGeneric: method.getReturnType() = " + type);
        log.debug("castGeneric: method.getGenericReturnType() = " + genericReturnType);
        if (data instanceof Map map) {
            if (Map.class.isAssignableFrom(type)) {
                Map resultMap = new HashMap();
                log.debug(genericReturnType.toString());
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    Class<?> valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                    log.debug("keyType : " + keyType);
                    log.debug("valueType : " + valueType);
                    map.forEach(
                            (k,v) -> {
                                Object key = cast(k, keyType);
                                Object value = cast(v, valueType);
                                resultMap.put(key, value);
                            }
                    );
                }
                return resultMap;
            }
            if (data instanceof JSONObject jsonObject){
                return jsonObject.toJavaObject(type);
            } else if (!Map.class.isAssignableFrom(type)){
                return new JSONObject(map).toJavaObject(type);
            }
            else {
                return type;
            }
        } else if (data instanceof List list) {
            Object[] array = list.toArray();
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                Object resultArray = Array.newInstance(componentType, array.length);
                for (int i = 0; i < array.length; i++) {
                    if (componentType.isPrimitive() || componentType.getPackageName().startsWith("java")) {
                        Array.set(resultArray, i, array[i]);
                    } else {
                        Object castObject = cast(array[i], componentType);
                        Array.set(resultArray, i, castObject);
                    }
                }
                return resultArray;
            } else if (List.class.isAssignableFrom(type)) {
                List<Object> resultList = new ArrayList<>(array.length);
                log.debug(genericReturnType.toString());
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Type actualType = parameterizedType.getActualTypeArguments()[0];
                    log.debug(actualType.toString());
                    for (Object o : array) {
                        resultList.add(cast(o, (Class<?>) actualType));
                    }
                } else {
                    resultList.addAll(Arrays.asList(array));
                }
                return resultList;
            } else {
                return null;
            }
        } else {
            return cast(data, type);
        }
    }
}

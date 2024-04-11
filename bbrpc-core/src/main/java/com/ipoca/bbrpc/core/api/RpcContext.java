package com.ipoca.bbrpc.core.api;

import com.ipoca.bbrpc.core.meta.InstanceMeta;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xubang
 * @Date 2024/3/22 16:29
 */
@Data
public class RpcContext {

    List<Filter> filters;

    Router<InstanceMeta> router;

    LoadBalancer<InstanceMeta> loadBalancer;

    private Map<String, String> parameters = new HashMap<>();

    public static ThreadLocal<Map<String,String>> ContextParameters = new ThreadLocal<>(){
        @Override
        protected Map<String,String> initialValue(){
            return new HashMap<>();
        }
    };

    public static void setContextParameter(String key, String value) {
        ContextParameters.get().put(key, value);
    }

    public static String getContextParameter(String key){
        return ContextParameters.get().get(key);
    }

    public static void removeContextParameter(String key){
        ContextParameters.get().remove(key);
    }

}

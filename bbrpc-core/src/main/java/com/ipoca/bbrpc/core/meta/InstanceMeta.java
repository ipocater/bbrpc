package com.ipoca.bbrpc.core.meta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;



/**
 * 描述服务元数据
 *
 * @Author xubang
 * @Date 2024/3/26 10:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstanceMeta {

    private String schema;
    private String host;
    private Integer port;
    private String context; // dubbo url?k1=v1

    private boolean status;// online or offline
    private Map<String, String> parameter = new HashMap<>(); // idc A B C

    public InstanceMeta(String schema, String host, Integer port, String context) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    public String toPath() {
        return String.format("%s_%d", host, port);
    }

    public static InstanceMeta http(String host, Integer port){
        return new InstanceMeta("http", host, port, "bbrpc");
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d/%s",schema, host, port,context);

    }

    public String toUrl() {
        return String.format("%s://%s:%d/%s",schema, host, port,context);

    }

    public InstanceMeta addParams(Map<String, String> params){
        this.getParameter().putAll(params);
        return this;
    }

    public String toMetas() {
        return JSON.toJSONString(this.getParameter());
    }
}

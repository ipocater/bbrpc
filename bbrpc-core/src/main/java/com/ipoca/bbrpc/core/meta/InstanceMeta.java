package com.ipoca.bbrpc.core.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
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
    private String context;

    private boolean status;// online or offline
    private Map<String, String> parameter;

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
        return new InstanceMeta("http", host, port, "");
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d%s",schema, host, port,context);

    }
}

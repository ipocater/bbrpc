package com.ipoca.bbrpc.core.registry;

import com.ipoca.bbrpc.core.meta.InstanceMeta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/25 14:45
 */
@Data
@AllArgsConstructor
public class Event {
    List<InstanceMeta> data;
}

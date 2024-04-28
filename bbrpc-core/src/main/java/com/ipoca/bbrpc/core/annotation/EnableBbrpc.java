package com.ipoca.bbrpc.core.annotation;

import com.ipoca.bbrpc.core.config.ConsumerConfig;
import com.ipoca.bbrpc.core.config.ProviderConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 组合一个入口
 *
 * @Author：xubang
 * @Date：2024/4/28 20:48
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({ProviderConfig.class, ConsumerConfig.class})
public @interface EnableBbrpc {

}

package com.ipoca.bbrpc.demo.consumer;

import com.ipoca.bbrpc.core.annotation.BBConsumer;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.stereotype.Component;

/**
 *@Author：xubang
 *@Date：2024/3/16  13:58
 */

@Component
public class Demo2 {

    @BBConsumer
    UserService userService2;

    public void test(){
        User user = userService2.findById(100);
        System.out.println(user);
    }
}

package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.demo.api.Order;
import com.ipoca.bbrpc.demo.api.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *@Author：xubang
 *@Date：2024/3/13  22:10
 */

@Component
@BBProvider
public class OrderServiceImpl implements OrderService {

    @Autowired
    Environment environment;

    @Override
    public Order findById(Integer id) {

        if (id == 404){
            throw new RuntimeException("404 not found");
        }

        return new Order(id.longValue(), 15.6f);
    }
}

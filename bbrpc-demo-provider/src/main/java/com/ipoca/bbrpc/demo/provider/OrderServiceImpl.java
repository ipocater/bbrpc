package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.annotation.BBProvider;
import com.ipoca.bbrpc.demo.api.Order;
import com.ipoca.bbrpc.demo.api.OrderService;
import org.springframework.stereotype.Component;

/**
 *@Author：xubang
 *@Date：2024/3/13  22:10
 */

@Component
@BBProvider
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer id) {
        return new Order(id.longValue(), 15.6f);
    }
}

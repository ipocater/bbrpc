package com.ipoca.bbrpc.demo.consumer;

import com.ipoca.bbrpc.core.annotation.BBConsumer;
import com.ipoca.bbrpc.core.consumer.ConsumerBootstrap;
import com.ipoca.bbrpc.core.consumer.ConsumerConfig;
import com.ipoca.bbrpc.demo.api.Order;
import com.ipoca.bbrpc.demo.api.OrderService;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ConsumerConfig.class})
public class BbrpcDemoConsumerApplication {

    @BBConsumer
    UserService userService;

    @BBConsumer
    OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(BbrpcDemoConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner consumer_runner(){
        return x -> {
//            User user = userService.findById(1);
//            System.out.println("RPC result userService.findByid(1) " + user);

//            Order order = orderService.findById(404);
//            System.out.println("RPC result orderService.findByid(404) " + order);

            System.out.println(userService.getName());
        };
    }

}

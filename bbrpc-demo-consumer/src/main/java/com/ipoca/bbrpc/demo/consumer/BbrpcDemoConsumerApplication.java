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

import java.util.Arrays;
import java.util.List;

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

//            System.out.println(userService.getId(new User(100,"bb")));
//
//            System.out.println(userService.getName());
//
//            System.out.println(Arrays.toString(userService.getIds()));
//
//            System.out.println(userService.getIdList());

//            System.out.println(userService.getUserList());

           // System.out.println(userService.getLongIds());

            System.out.println(" ===> userService.getLongIds()");
            for (long id : userService.getIds(new int[]{4,5,6})){
                System.out.println(id);
            }

//            User user = userService.findById(1);
//            System.out.println("RPC result userService.findByid(1) " + user);
//
//            User user1 = userService.findById(1,"xubang");
//            System.out.println("RPC result userService.findByid(1,xubang) " + user1);
//
////            Order order = orderService.findById(404);
////            System.out.println("RPC result orderService.findByid(404) " + order);
//
//            System.out.println(userService.getName());
//
//            System.out.println(userService.getName(123));

        };
    }

}

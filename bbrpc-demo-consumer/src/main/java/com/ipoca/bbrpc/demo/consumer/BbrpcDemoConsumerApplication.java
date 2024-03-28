package com.ipoca.bbrpc.demo.consumer;

import com.ipoca.bbrpc.core.annotation.BBConsumer;
import com.ipoca.bbrpc.core.consumer.ConsumerConfig;
import com.ipoca.bbrpc.demo.api.OrderService;
import com.ipoca.bbrpc.demo.api.User;
import com.ipoca.bbrpc.demo.api.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ConsumerConfig.class})
public class BbrpcDemoConsumerApplication {

    @BBConsumer
    UserService userService;

    @BBConsumer
    OrderService orderService;


    @RequestMapping("/")
    public User findById(int id){
        return userService.findById(id);
    }

    public static void main(String[] args) {
        SpringApplication.run(BbrpcDemoConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner consumer_runner(){
        return x -> {
//            System.out.println(" ===> userService.getLongIds()");
//            for (long id : userService.getIds(new int[]{4,5,6})){
//                System.out.println(id);
//            }


        };
    }

}

package com.ipoca.bbrpc.demo.consumer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BbrpcDemoConsumerApplicationTests {

    static ApplicationContext context;

    @BeforeAll
    static void init(){
        //context = SpringApplication.run()
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> aaaa .....");
    }

    @AfterAll
    static void destory(){
        SpringApplication.exit(context, () -> 1);
    }

}

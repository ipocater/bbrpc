package com.ipoca.bbrpc.demo.consumer;

import com.ipoca.bbrpc.core.test.TestZKServer;
import com.ipoca.bbrpc.demo.provider.BbrpcDemoProviderApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BbrpcDemoConsumerApplicationTests {

    static ApplicationContext context;

    static TestZKServer zkServer = new TestZKServer();

    @BeforeAll
    static void init(){
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");

        zkServer.start();
        context = SpringApplication.run(BbrpcDemoProviderApplication.class,
                "--server.port=8094", "--bbrpc.zkServer=localhost:2182",
                "--logging.level.com.ipoca.bbrpc=info");
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> aaaa .....");
    }

    @AfterAll
    static void destory(){
        SpringApplication.exit(context, () -> 1);
        zkServer.stop();
    }

}

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

    static ApplicationContext context1;

    static ApplicationContext context2;

    static TestZKServer zkServer = new TestZKServer();

    @BeforeAll
    static void init(){
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" =========  ZK2182   ========= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        zkServer.start();
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" =========  P8094   ========= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        context2 = SpringApplication.run(BbrpcDemoProviderApplication.class,
                "--server.port=8094", "--bbrpc.zkServer=localhost:2182",
                "--logging.level.com.ipoca.bbrpc=info");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        System.out.println(" =========  P8095   ========= ");
        System.out.println(" ============================= ");
        System.out.println(" ============================= ");
        context2 = SpringApplication.run(BbrpcDemoProviderApplication.class,
                "--server.port=8095", "--bbrpc.zkServer=localhost:2182",
                "--logging.level.com.ipoca.bbrpc=info");
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> aaaa .....");
    }

    @AfterAll
    static void destory(){
        SpringApplication.exit(context1, () -> 1);
        SpringApplication.exit(context2, () -> 1);
        zkServer.stop();
    }

}

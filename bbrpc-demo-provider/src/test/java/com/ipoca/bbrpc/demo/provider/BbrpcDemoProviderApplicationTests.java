package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.test.TestZKServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BbrpcDemoProviderApplicationTests {

    static TestZKServer zkServer = new TestZKServer();

    @BeforeAll
    static void init(){
        zkServer.start();
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> BbrpcDemoProviderApplicationTests  ... ");
    }

    @AfterAll
    static void destory() {
        zkServer.stop();
    }

}

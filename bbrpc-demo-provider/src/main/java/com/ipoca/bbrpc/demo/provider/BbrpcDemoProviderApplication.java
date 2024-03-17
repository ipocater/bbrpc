package com.ipoca.bbrpc.demo.provider;

import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.provider.ProviderBootstrap;
import com.ipoca.bbrpc.core.provider.ProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Import({ProviderConfig.class})
public class BbrpcDemoProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BbrpcDemoProviderApplication.class, args);
    }

    //使用HTTP + JSON 来实现序列化和通信

    @Autowired
    ProviderBootstrap providerBootstrap;

    @RequestMapping("/")
    public RpcResponse invoke(@RequestBody RpcRequest request){
        return providerBootstrap.invoke(request);
    }

    @Bean
    ApplicationRunner providerRun() {
        return x -> {
            RpcRequest request = new RpcRequest();
            request.setService("com.ipoca.bbrpc.demo.api.UserService");
            request.setMethodSign("findById@1_int");
            request.setArgs(new Object[]{100});

            RpcResponse rpcResponse = providerBootstrap.invoke(request);
            System.out.println("return : " + rpcResponse.getData());
        };
    }
}

package com.ipoca.bbrpc.core.transport;

import com.ipoca.bbrpc.core.api.RpcRequest;
import com.ipoca.bbrpc.core.api.RpcResponse;
import com.ipoca.bbrpc.core.provider.ProviderInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transport for
 *
 *@Author：xubang
 *@Date：2024/4/12  0:43
 */

@RestController
public class SpringBootTransport {

    @Autowired
    ProviderInvoker providerInvoker;

    @RequestMapping("/bbrpc")
    public RpcResponse<Object> invoke(@RequestBody RpcRequest request){
        return providerInvoker.invoke(request);
    }

}

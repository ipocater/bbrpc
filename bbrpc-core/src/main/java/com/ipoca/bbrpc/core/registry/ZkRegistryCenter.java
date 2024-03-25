package com.ipoca.bbrpc.core.registry;

import com.ipoca.bbrpc.core.api.RegistryCenter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.List;

/**
 * @Author xubang
 * @Date 2024/3/22 17:31
 */
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;
    @Override
    public void start() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .namespace("bbrpc")
                .retryPolicy(retryPolicy)
                .build();
        System.out.println(" ===> zk client starting.");
        client.start();
    }

    @Override
    public void stop() {
        System.out.println(" ===> zk client stopped." );
        client.close();
    }

    @Override
    public void register(String service, String instance) {
        String servicePath = "/" + service;
        try {
            // 创建服务的持久化节点
            if (client.checkExists().forPath(servicePath) == null){
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath,"service".getBytes());
            }
            // 创建实例的临时性节点
            String instancePath = servicePath + "/" + instance;
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath,"provider".getBytes());
            System.out.println(" ===> register to zk: " + instancePath);
        } catch (Exception ex){
            throw new RuntimeException();
        }
    }

    @Override
    public void unregister(String service, String instance) {
        String servicePath = "/" + service;
        try {
            // 判断服务是否存在
            if (client.checkExists().forPath(servicePath) == null){
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance;
            System.out.println(" ===> unregister to zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> fetchAll(String service) {
        return null;
    }
}

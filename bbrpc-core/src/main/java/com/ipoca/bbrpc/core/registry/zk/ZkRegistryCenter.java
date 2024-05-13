package com.ipoca.bbrpc.core.registry.zk;

import com.alibaba.fastjson.JSON;
import com.ipoca.bbrpc.core.api.RpcException;
import com.ipoca.bbrpc.core.api.RegistryCenter;
import com.ipoca.bbrpc.core.meta.InstanceMeta;
import com.ipoca.bbrpc.core.meta.ServiceMeta;
import com.ipoca.bbrpc.core.registry.ChangedListener;
import com.ipoca.bbrpc.core.registry.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xubang
 * @Date 2024/3/22 17:31
 */
@Slf4j
public class ZkRegistryCenter implements RegistryCenter {
    @Value("${bbrpc.zk.server}")
    String servers;

    @Value("${bbrpc.zk.root}")
    String root;

    private CuratorFramework client = null;
    @Override
    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
        client = CuratorFrameworkFactory.builder()
                .connectString(servers)
                .namespace(root) // dubbo的group就是这个
                .retryPolicy(retryPolicy)
                .build();
        log.info(" ===> zk client starting to server[" + servers + "/" + root +"].");
        client.start();
    }

    @Override
    public void stop() {
        log.info(" ===> zk client stopped." );
        client.close();
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toPath();
        try {
            // 创建服务的持久化节点
            if (client.checkExists().forPath(servicePath) == null){
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath,service.toMetas().getBytes());
            }
            // 创建实例的临时性节点
            String instancePath = servicePath + "/" + instance.toPath();
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath,instance.toMetas().getBytes());
            log.info(" ===> register to zk: " + instancePath);
        } catch (Exception ex){
            throw new RpcException();
        }
    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        String servicePath = "/" + service.toPath();
        try {
            // 判断服务是否存在
            if (client.checkExists().forPath(servicePath) == null){
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info(" ===> unregister to zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception ex) {
            throw new RpcException();
        }
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        String servicePath = "/" + service.toPath();
        try {
            // 获取所有子节点
            List<String> nodes = client.getChildren().forPath(servicePath);
            log.info(" ===> fetchAll to zk: " + servicePath);
            nodes.forEach(System.out::println);
            return mapInstances(nodes, servicePath);
        } catch (Exception ex) {
            throw new RpcException();
        }
    }

    @NotNull
    private List<InstanceMeta> mapInstances(List<String> nodes,String servicePath) {
        return nodes.stream().map(x -> {

            String[] strs = x.split("_");
            InstanceMeta instance = InstanceMeta.http(strs[0], Integer.valueOf(strs[1]));
            System.out.println(" instance: " + instance.toUrl());
            String nodePath = servicePath + "/" + x;
            byte[] bytes;
            try {
                bytes = client.getData().forPath(nodePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            HashMap params = JSON.parseObject(new String(bytes), HashMap.class);
            params.forEach((k,v) -> System.out.println(k + " -> " + v));
            instance.setParameter(params);
            return instance;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {
        final TreeCache cache = TreeCache.newBuilder(client, "/"+service.toPath())
                .setCacheData(true)
                .setMaxDepth(2)
                .build();
        cache.getListenable().addListener(
                (curator, event) -> {
                    // 有任何节点变动这里会执行
                    log.info("zk subscribe event: " + event);
                    List<InstanceMeta> nodes = fetchAll(service);
                    listener.fire(new Event(nodes));
                }
        );
        cache.start();
    }
}

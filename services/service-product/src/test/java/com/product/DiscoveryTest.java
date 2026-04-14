package com.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
public class DiscoveryTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 测试服务发现：获取所有注册到 Nacos 的服务
     */
    @Test
    public void testGetServices() {
        // 获取所有服务名称
        List<String> services = discoveryClient.getServices();
        System.out.println("========== 所有注册的服务 ==========");
        services.forEach(service -> {
            System.out.println("服务名称: " + service);
        });
    }

    /**
     * 测试服务发现：获取指定服务的所有实例
     */
    @Test
    public void testGetInstances() {
        // 获取 server-product 服务的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("server-product");
        System.out.println("========== server-product 服务实例 ==========");
        instances.forEach(instance -> {
            System.out.println("实例ID: " + instance.getInstanceId());
            System.out.println("服务名: " + instance.getServiceId());
            System.out.println("Host: " + instance.getHost());
            System.out.println("Port: " + instance.getPort());
            System.out.println("URI: " + instance.getUri());
            System.out.println("Metadata: " + instance.getMetadata());
            System.out.println("-----------------------------------");
        });
    }

    /**
     * 测试服务发现：获取 server-order 服务实例
     */
    @Test
    public void testGetOrderInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances("server-order");
        System.out.println("========== server-order 服务实例 ==========");
        instances.forEach(instance -> {
            System.out.println("服务地址: " + instance.getHost() + ":" + instance.getPort());
        });
    }
}

package com.order;

import com.order.feign.ProductFeignClient;
import com.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.List;

@SpringBootTest
public class LoadBalancerTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 测试服务发现：手动获取服务实例
     */
    @Test
    public void testDiscoveryClient() {
        List<ServiceInstance> instances = discoveryClient.getInstances("server-product");
        System.out.println("========== 手动服务发现 ==========");
        instances.forEach(instance -> {
            System.out.println("服务地址: " + instance.getHost() + ":" + instance.getPort());
        });
    }

    /**
     * 测试负载均衡：使用 LoadBalancerClient 选择服务实例
     */
    @Test
    public void testLoadBalancer() {
        System.out.println("========== 负载均衡测试 ==========");
        // 多次调用，观察负载均衡效果（如果有多个实例）
        for (int i = 0; i < 5; i++) {
            ServiceInstance instance = loadBalancerClient.choose("server-product");
            System.out.println("第" + (i + 1) + "次调用，选择的实例: " +
                instance.getHost() + ":" + instance.getPort());
        }
    }

    /**
     * 测试 OpenFeign 远程调用
     */
    @Test
    public void testFeignClient() {
        System.out.println("========== OpenFeign 远程调用测试 ==========");
        Product product = productFeignClient.getProductById(100L);
        System.out.println("商品信息: " + product);
    }
}

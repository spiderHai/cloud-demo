package com.order.service.impl;

import com.order.Order;
import com.order.service.OrderService;
import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderServiceDiscoveryClientImpl {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 方式1：使用 DiscoveryClient 手动服务发现
     * 缺点：需要手动拼接 URL，没有负载均衡
     */
    public Product getProductByDiscoveryClient(Long productId) {
        // 1. 从 Nacos 获取 server-product 的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("server-product");

        if (instances.isEmpty()) {
            throw new RuntimeException("没有可用的 server-product 实例");
        }

        // 2. 简单取第一个实例（没有负载均衡）
        ServiceInstance instance = instances.get(0);

        // 3. 手动拼接 URL
        String url = "http://" + instance.getHost() + ":" + instance.getPort()
                   + "/product/getProduct/" + productId;

        log.info("调用服务: {}", url);

        // 4. 发起 HTTP 请求
        return restTemplate.getForObject(url, Product.class);
    }
}

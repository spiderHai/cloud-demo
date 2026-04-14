package com.order.service.impl;

import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderServiceLoadBalancerImpl {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 方式2：使用 LoadBalancerClient 实现负载均衡
     * 优点：自动负载均衡（默认轮询）
     * 缺点：仍需手动拼接 URL
     */
    public Product getProductByLoadBalancer(Long productId) {
        // 1. LoadBalancerClient 自动从多个实例中选择一个（轮询策略）
        ServiceInstance instance = loadBalancerClient.choose("server-product");

        if (instance == null) {
            throw new RuntimeException("没有可用的 server-product 实例");
        }

        // 2. 手动拼接 URL
        String url = "http://" + instance.getHost() + ":" + instance.getPort()
                   + "/product/getProduct/" + productId;

        log.info("负载均衡选择的实例: {}:{}", instance.getHost(), instance.getPort());

        // 3. 发起 HTTP 请求
        return restTemplate.getForObject(url, Product.class);
    }
}

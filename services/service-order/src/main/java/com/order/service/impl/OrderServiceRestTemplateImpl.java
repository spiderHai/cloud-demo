package com.order.service.impl;

import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderServiceRestTemplateImpl {

    @Autowired
    private RestTemplate restTemplate;  // 注入的是带 @LoadBalanced 的 RestTemplate

    /**
     * 方式3：使用 @LoadBalanced 注解的 RestTemplate
     * 优点：
     * 1. 直接使用服务名，不需要手动拼接 IP:Port
     * 2. 自动负载均衡（默认轮询）
     * 3. 代码简洁
     *
     * 原理：
     * @LoadBalanced 会给 RestTemplate 添加拦截器
     * 拦截器会将服务名解析为实际的 IP:Port
     */
    public Product getProductByRestTemplate(Long productId) {
        // 直接使用服务名，不是 IP:Port
        String url = "http://server-product/product/getProduct/" + productId;

        log.info("使用 @LoadBalanced RestTemplate 调用: {}", url);

        // RestTemplate 会自动：
        // 1. 从 Nacos 获取 server-product 的实例列表
        // 2. 通过负载均衡算法选择一个实例
        // 3. 将 server-product 替换为实际的 IP:Port
        // 4. 发起 HTTP 请求
        return restTemplate.getForObject(url, Product.class);
    }
}

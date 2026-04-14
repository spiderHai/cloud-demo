package com.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * 创建 RestTemplate Bean
     * @LoadBalanced 注解：启用负载均衡功能
     *
     * 作用：
     * 1. 拦截 RestTemplate 的请求
     * 2. 将服务名（如 server-product）解析为实际的 IP:Port
     * 3. 自动实现负载均衡（默认轮询策略）
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

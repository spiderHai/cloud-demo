package com.order;

import com.order.service.impl.OrderServiceDiscoveryClientImpl;
import com.order.service.impl.OrderServiceLoadBalancerImpl;
import com.order.service.impl.OrderServiceRestTemplateImpl;
import com.order.feign.ProductFeignClient;
import com.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RemoteCallTest {

    @Autowired
    private OrderServiceDiscoveryClientImpl discoveryClientImpl;

    @Autowired
    private OrderServiceLoadBalancerImpl loadBalancerImpl;

    @Autowired
    private OrderServiceRestTemplateImpl restTemplateImpl;

    @Autowired
    private ProductFeignClient productFeignClient;

    /**
     * 方式1：DiscoveryClient 手动服务发现
     * 特点：需要手动拼接 URL，没有负载均衡
     */
    @Test
    public void testDiscoveryClient() {
        System.out.println("========== 方式1：DiscoveryClient 手动服务发现 ==========");
        for (int i = 0; i < 3; i++) {
            Product product = discoveryClientImpl.getProductByDiscoveryClient(100L);
            System.out.println("第" + (i + 1) + "次调用结果: " + product.getProductName());
        }
    }

    /**
     * 方式2：LoadBalancerClient 手动负载均衡
     * 特点：自动负载均衡，但仍需手动拼接 URL
     */
    @Test
    public void testLoadBalancerClient() {
        System.out.println("========== 方式2：LoadBalancerClient 手动负载均衡 ==========");
        for (int i = 0; i < 3; i++) {
            Product product = loadBalancerImpl.getProductByLoadBalancer(100L);
            System.out.println("第" + (i + 1) + "次调用结果: " + product.getProductName());
        }
    }

    /**
     * 方式3：@LoadBalanced RestTemplate
     * 特点：直接使用服务名，自动负载均衡，代码简洁
     */
    @Test
    public void testLoadBalancedRestTemplate() {
        System.out.println("========== 方式3：@LoadBalanced RestTemplate ==========");
        for (int i = 0; i < 3; i++) {
            Product product = restTemplateImpl.getProductByRestTemplate(100L);
            System.out.println("第" + (i + 1) + "次调用结果: " + product.getProductName());
        }
    }

    /**
     * 方式4：OpenFeign（最推荐）
     * 特点：声明式调用，代码最简洁，功能最强大
     */
    @Test
    public void testOpenFeign() {
        System.out.println("========== 方式4：OpenFeign（最推荐） ==========");
        for (int i = 0; i < 3; i++) {
            Product product = productFeignClient.getProductById(100L);
            System.out.println("第" + (i + 1) + "次调用结果: " + product.getProductName());
        }
    }

    /**
     * 对比测试：多次调用观察负载均衡效果
     * 如果启动多个 server-product 实例，可以看到请求分发到不同实例
     */
    @Test
    public void testLoadBalancingEffect() {
        System.out.println("========== 负载均衡效果测试（需要启动多个实例） ==========");
        System.out.println("提示：启动多个 server-product 实例（修改端口），观察负载均衡效果");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            Product product = productFeignClient.getProductById(100L);
            System.out.println("第" + (i + 1) + "次调用成功");
        }
    }
}

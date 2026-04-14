package com.order.service.impl;

import com.order.Order;
import com.order.feign.ProductFeignClient;
import com.order.service.OrderService;
import com.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Order createOrder(Long productId, Long userId) {
        log.info("创建订单，productId: {}, userId: {}", productId, userId);

        // 通过OpenFeign远程调用商品服务
        Product product = productFeignClient.getProductById(productId);

        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNickName("小明");
        order.setAddress("成都");
        order.setProducts(List.of(product));

        return order;
    }
}

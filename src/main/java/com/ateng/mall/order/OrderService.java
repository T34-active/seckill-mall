package com.ateng.mall.order;

import com.ateng.mall.auth.UserPrincipal;
import com.ateng.mall.common.BizException;
import com.ateng.mall.product.Product;
import com.ateng.mall.product.ProductRepository;
import com.ateng.mall.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;

    /**
     * 下单主链路：
     * 1) Redis Lua 原子扣库存（防超卖）
     * 2) 写订单 + 回写 DB 库存快照
     * 3) DB 失败则回滚 Redis 库存
     */
    @Transactional
    public OrderVO createOrder(UserPrincipal principal, CreateOrderRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BizException("商品不存在"));
        if (!Boolean.TRUE.equals(product.getEnabled())) {
            throw new BizException("商品已下架");
        }

        int quantity = request.getQuantity();
        boolean deducted = false;
        try {
            stockService.deduct(product.getId(), quantity);
            deducted = true;

            OrderEntity order = new OrderEntity();
            order.setOrderNo(generateOrderNo());
            order.setUserId(principal.getId());
            order.setProductId(product.getId());
            order.setProductName(product.getName());
            order.setQuantity(quantity);
            order.setUnitPrice(product.getPrice());
            order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            order.setStatus("CREATED");
            orderRepository.save(order);

            // DB 库存快照与 Redis 对齐（教学项目：简单回写；生产可异步对账）
            Integer redisLeft = stockService.getStock(product.getId());
            if (redisLeft != null) {
                product.setStock(redisLeft);
                productRepository.save(product);
            }

            return toVO(order);
        } catch (RuntimeException ex) {
            if (deducted) {
                stockService.rollback(product.getId(), quantity);
            }
            throw ex;
        }
    }

    public List<OrderVO> myOrders(Long userId) {
        List<OrderEntity> orders = orderRepository.findByUserIdOrderByIdDesc(userId);
        List<OrderVO> result = new ArrayList<OrderVO>();
        for (OrderEntity order : orders) {
            result.add(toVO(order));
        }
        return result;
    }

    public OrderVO getOrder(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BizException("订单不存在"));
        if (!order.getUserId().equals(userId)) {
            throw new BizException(403, "无权查看该订单");
        }
        return toVO(order);
    }

    private OrderVO toVO(OrderEntity order) {
        return OrderVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .unitPrice(order.getUnitPrice())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "O" + time + rand + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}

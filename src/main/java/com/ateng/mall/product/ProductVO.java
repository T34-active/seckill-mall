package com.ateng.mall.product;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductVO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Integer redisStock;
}

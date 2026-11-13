package com.ateng.mall.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class testProduct {
    private Long id;
    private String name;
    private Integer stock;
    public static void main(String[] args) {
        testProduct myProduct = new testProduct();
        myProduct.setId(1L);
        myProduct.setName("秒杀手机");
        myProduct.setStock(100);
        System.out.println(myProduct.getId());
        System.out.println(myProduct.getName());
        System.out.println(myProduct.getStock());
    }
}

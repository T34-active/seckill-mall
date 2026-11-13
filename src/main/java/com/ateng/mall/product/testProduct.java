package com.ateng.mall.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class testProduct {
  //    private Number id;
  //    private String name;
  //    private Integer stock;

  public static void main(String[] args) {
    // 题 T1 · `int`
    //        int b = 3;
    //        int a = 10;
    //        System.out.println(a + b);
    //        System.out.println(a - b);
    //        System.out.println(a * b);
    // 题 T2 · `long` / `Long`（重点）
    //        long x = 10L;
    //        long y = 10L;
    //        System.out.println(x * y);
    //     题 T3 · `double`
    //    double price = 88.999;
    //    System.out.println("价格：" + price);
    // 题 T4 · `boolean`
    //    boolean enough = true;
    //    if (!enough) {
    //      System.out.println("库存不足");
    //    } else {
    //      System.out.println("可以下单");
    //    }
    // 题 T5 · `char` vs `String`（重点）
    //    char c = '秒';
    //    String name = "秒123123";
    //    String bad = "秒杀手机";
    //    System.out.println(c);
    //    System.out.println(name);
    //    System.out.println(bad);
    //        testProduct myProduct = new testProduct();
    //        myProduct.setId(1000);
    //        myProduct.setName("秒杀手机");
    //        myProduct.setStock(100);
    //        System.out.println(myProduct.getId());
    //        System.out.println(myProduct.getName());
    //        System.out.println(myProduct.getStock());
  }
  // 在没有签名getter和setter的时候使用
  //    private void setId() {
  //        this.id = 1000;
  //    }
  //    private void setName() {
  //        this.name = "秒杀手机";
  //    }
  //    private void setStock() {
  //        this.stock = 100;
  //    }
  //    private Integer getStock() {
  //        return this.stock;
  //    }
  //    private String getName() {
  //        return this.name;
  //    }
  //    private Number getId() {
  //        return this.id;
  //    }
}

# 练习题 · Java 基础（结合 seckill-mall）

按你当前学到的内容出题，由易到难。  
建议：先自己写 → 跑通 → 再对照项目里的真实类。

有 JS/TS 背景时，可把每题的 Java 写法在脑子里映射成 TS 类再写。

---

## 题 1：实体小抄

写一个类（练习用，不要和项目里的 `Product` 重名，可叫 `ProductPractice`），要求：

- 字段：`Long id`、`String name`、`Integer stock`
- 用 Lombok 的 `@Getter`、`@Setter`
- 在 `main` 里创建一个对象，给字段赋值，再打印 `name` 和 `stock`

**目的**：熟悉类、字段、get/set。

**提示**：`public` 类名必须和文件名一致；有了 `@Setter` 就不用再手写 `setXxx`。

**参考位置（练完可对照）**：`src/main/java/com/ateng/mall/product/Product.java`

---

## 题 2：构造方法

写一个类 `UserPractice`：

- 字段：`String username`、`String role`
- 写一个构造方法：`UserPractice(String username, String role)`
- 再写一个方法：`String describe()`，返回类似 `"用户 admin，角色 ADMIN"`
- `main` 里 `new` 一个并打印

**目的**：熟悉构造方法、`this`、字符串拼接。

**参考位置**：`src/main/java/com/ateng/mall/user/User.java`

---

## 题 3：接口与实现（模仿 UserPrincipal）

定义接口：

```java
public interface AccountStatus {
    boolean isEnabled();
}
```

再写类 `SimpleAccount` 实现它：

- 字段：`boolean enabled`
- 构造方法传入 `enabled`
- `isEnabled()` 返回这个字段

`main` 里分别创建 `true` / `false` 各一个，打印结果。

**目的**：熟悉 `implements`、`@Override`。

**参考位置**：`src/main/java/com/ateng/mall/auth/UserPrincipal.java`

---

## 题 4：简单「扣库存」

写方法：

```java
/**
 * 库存够就扣，不够返回 false
 * @return true=扣成功，false=库存不足
 */
public static boolean deduct(int[] stock, int amount)
```

规则：

- `stock[0]` 表示当前库存
- 如果 `stock[0] >= amount`，就减去并返回 `true`
- 否则返回 `false`

`main` 里测两组：库存 10 扣 3；库存 2 扣 5。

**目的**：熟悉数组、`if`、返回值（后面下单扣库存会用到同样思路）。

**参考位置**：`src/main/java/com/ateng/mall/stock/StockService.java`

---

## 题 5：小综合（最接近项目）

写类 `OrderRequestPractice`：

- 字段：`Long productId`、`Integer quantity`
- `@Getter` / `@Setter`

再写方法：

```java
public static String validate(OrderRequestPractice req)
```

规则：

- `productId == null` → 返回 `"商品不能为空"`
- `quantity == null || quantity <= 0` → 返回 `"数量必须大于0"`
- 都合法 → 返回 `"OK"`

`main` 里测 3 种情况。

**目的**：空值判断、业务校验（Controller/Service 里常见）。

**参考位置**：`src/main/java/com/ateng/mall/order/CreateOrderRequest.java`

---

## 建议做题顺序

1. 先做 **题 1 → 题 2 → 题 3**
2. 有余力再做 **题 4、题 5**
3. 练习类可放在 `src/main/java/com/ateng/mall/product/` 或单独建 `practice` 包，**不要覆盖**项目正式类名

## 自检清单

- [ ] 题 1：能打印出商品名和库存
- [ ] 题 2：构造后 `describe()` 输出正确
- [ ] 题 3：两个对象分别打印 true / false
- [ ] 题 4：第一组成功、第二组失败
- [ ] 题 5：三种校验结果都对

写完可贴代码给 AI 检查，或说「我做到第几题了」。

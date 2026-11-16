# 练习题 · Java 类型（一个个学）

按顺序做：**一题只练一个类型（或一对基本类型/包装类）**。  
建议位置：`src/main/java/com/ateng/mall/product/TypeDrill.java`（类名和文件名要一致）。

先看：[01-常见类型快速上手.md](./01-常见类型快速上手.md)

有 JS/TS 背景时，把每题在脑子里映射成 TS 再写。

---

## 第一阶段：八个基本类型 + String（必做）

### 题 T1 · `int`

在 `main` 里：

- 声明 `int a = 10;`、`int b = 3;`
- 打印 `a + b`、`a - b`、`a * b`

**自检：** 能输出三个计算结果。

---

### 题 T2 · `long` / `Long`（重点）

- 声明 `long x = 10L;`
- 声明 `Long y = 10L;`
- 试着写一行 `Long z = 10;`，看编译器报错，然后改成正确写法
- 打印 `x` 和 `y`

**自检：** 知道为什么必须加 `L`（或先转成 `long`）。

**易错回顾：** `Long` 不能直接接裸的 `int` 字面量 `10`。

---

### 题 T3 · `double`

- 声明 `double price = 99.9;`
- 打印 `"价格：" + price`

**自检：** 能打印带小数的价格。

---

### 题 T4 · `boolean`

- 声明 `boolean enough = true;`
- 用 `if (!enough)` / `else` 打印「库存不足」或「可以下单」

**自检：** 改成 `false` 再跑一次，输出会变。

---

### 题 T5 · `char` vs `String`（重点）

- 声明 `char c = '秒';`（单引号、一个字）
- 声明 `String name = "秒杀手机";`（双引号）
- 故意写 `String bad = '秒杀手机';`，看报错「字符字面量中的字符数过多」
- 改对后打印 `c` 和 `name`

**自检：** 能口头说出单引号和双引号的区别。

---

### 题 T6 · `byte` / `short` / `float`（扫一眼）

各声明一个变量并打印即可：

```java
byte b = 100;
short s = 1000;
float f = 1.5f;
```

**自检：** 知道业务里更常用 `int` / `long` / `double`，这三个先混个脸熟。

---

### 题 T7 · 包装类 `Integer` 与 `null`

- `Integer stock = 100;`
- `Integer empty = null;`
- 打印 `stock`
- 用 `if (empty == null)` 打印「空库存标记」

**自检：** 理解「包装类才能 null，`int` 不行」。

---

## 第二阶段：组合使用（巩固）

### 题 T8 · 数组 `int[]`

写方法（可放在同一个练习类里）：

```java
public static boolean deduct(int[] stock, int amount)
```

- `stock[0] >= amount` → 扣减并返回 `true`
- 否则返回 `false`

`main` 测两组：库存 10 扣 3；库存 2 扣 5。

**对照项目：** `StockService`（真实扣减在 Redis，这里只练数组思路）。

---

### 题 T9 · `List<String>`

- `new ArrayList<>()`，`add` 两个商品名
- 打印 `get(0)` 和 `size()`

**对照项目：** 返回 `List<ProductVO>` 的接口。

---

### 题 T10 · Lombok 小实体（类型大团圆）

写类 `ProductPractice`（不要覆盖正式的 `Product`）：

- 字段：`Long id`、`String name`、`Integer stock`
- `@Getter` `@Setter`
- `main` 里：`setId(1L)`、`setName("秒杀手机")`、`setStock(100)`，再打印三个 getter

**自检：**

- [ ] 没用单引号写中文名
- [ ] `id` 用了 `1L` 或能通过编译的写法
- [ ] 没有再手写同签名的 get/set

**对照项目：** `Product.java`

---

### 题 T11 · 校验（`Long` + `Integer` + `null`）

```java
public static String validate(Long productId, Integer quantity)
```

- `productId == null` → `"商品不能为空"`
- `quantity == null || quantity <= 0` → `"数量必须大于0"`
- 否则 → `"OK"`

`main` 测 3 种情况。

**对照项目：** `CreateOrderRequest` / 下单入参校验。

---

## 第三阶段：选做（有余力）

### 题 T12 · 构造方法

`UserPractice(String username, String role)` + `describe()` 返回 `"用户 admin，角色 ADMIN"`。

### 题 T13 · 接口

`AccountStatus` + `SimpleAccount`，实现 `boolean isEnabled()`。

### 题 T14 · 认识 `BigDecimal`

`BigDecimal price = new BigDecimal("99.90");` 打印即可（先不用算加减）。

---

## 建议做题顺序

```
T1 int → T2 long/Long → T3 double → T4 boolean → T5 char/String
→ T6 扫脸 → T7 Integer/null → T8 数组 → T9 List → T10 实体 → T11 校验
```

卡住就把报错原文贴给 AI；说「我做到 T几」即可继续讲。

## 总自检清单

- [ ] T1 `int` 运算
- [ ] T2 `long`/`Long` + `L`
- [ ] T3 `double`
- [ ] T4 `boolean` + if
- [ ] T5 单引号 vs 双引号
- [ ] T6 byte/short/float 混脸熟
- [ ] T7 包装类 null
- [ ] T8 数组扣库存
- [ ] T9 List
- [ ] T10 Lombok 实体
- [ ] T11 null 校验

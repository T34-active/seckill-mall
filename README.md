# seckill-mall

Spring Boot 教学项目：JWT 登录鉴权 + 商品下单 + Redis Lua 原子扣库存。

## 技术栈

- Java 8 / Spring Boot 2.7.18
- Spring Security + JWT
- Spring Data JPA + MySQL 8
- Redis（Lua 脚本原子扣库存）

## 启动前准备

1. MySQL 已启动，库名 `seckill_mall`（可用 `scripts/schema-mysql.sql` 建库建表）
2. Redis 已启动（本机 `127.0.0.1:6379`，密码默认 `123456`）
3. JDK 8+、Maven 3.8+

### Navicat 连接

| 项     | 值             |
|--------|----------------|
| 主机   | `127.0.0.1`    |
| 端口   | `3306`         |
| 用户   | `root`         |
| 密码   | `123456`       |
| 数据库 | `seckill_mall` |

主要表：`t_user` / `t_product` / `t_order`

```bash
cd seckill-mall
mvn spring-boot:run
# 默认激活 local；切换环境示例：
# mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=test
# java -jar target/seckill-mall-1.0.0.jar --spring.profiles.active=prod
```

配置文件：

| 文件                    | 环境                |
|-------------------------|---------------------|
| `application.yml`       | 公共配置 + 字段说明 |
| `application-local.yml` | 本地开发（默认）    |
| `application-test.yml`  | 测试                |
| `application-prod.yml`  | 线上                |

服务地址：`http://127.0.0.1:8080`

演示账号：`demo` / `123456`

## 核心接口

| 方法 | 路径                 | 鉴权 | 说明                      |
|------|----------------------|------|---------------------------|
| POST | `/api/auth/register` | 否   | 注册并返回 token          |
| POST | `/api/auth/login`    | 否   | 登录拿 token              |
| GET  | `/api/auth/me`       | 是   | 当前用户                  |
| GET  | `/api/products`      | 否   | 商品列表（含 Redis 库存） |
| GET  | `/api/products/{id}` | 否   | 商品详情                  |
| POST | `/api/orders`        | 是   | 下单（Redis 扣库存）      |
| GET  | `/api/orders`        | 是   | 我的订单                  |
| GET  | `/api/orders/{id}`   | 是   | 订单详情                  |

## 快速验证

```powershell
# 登录
$login = Invoke-RestMethod -Method Post -Uri http://127.0.0.1:8080/api/auth/login `
  -ContentType 'application/json' -Body '{"username":"demo","password":"123456"}'
$token = $login.data.token

# 商品列表
Invoke-RestMethod -Uri http://127.0.0.1:8080/api/products

# 下单
Invoke-RestMethod -Method Post -Uri http://127.0.0.1:8080/api/orders `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType 'application/json' -Body '{"productId":1,"quantity":1}'
```

或执行：`powershell -File scripts/smoke-test.ps1`

## 下单链路（逆向拆解入口）

```
客户端
  -> JwtAuthFilter 解析 Bearer Token
  -> OrderController.create
  -> OrderService.createOrder
       1. Redis Lua 原子扣库存（StockService.deduct）
       2. 写 H2 订单 + 回写商品库存快照
       3. 失败则 Redis rollback
```

学习文档见同目录：`导学-秒杀商城.md`、`面经-秒杀商城.md`。

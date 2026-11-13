-- Navicat / MySQL 建库建表（含注释）
CREATE DATABASE IF NOT EXISTS seckill_mall
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE seckill_mall;

CREATE TABLE IF NOT EXISTS t_user (
  id         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username   VARCHAR(64)  NOT NULL COMMENT '登录用户名',
  password   VARCHAR(100) NOT NULL COMMENT '密码(BCrypt)',
  role       VARCHAR(32)  NOT NULL COMMENT '角色: USER/ADMIN',
  created_at DATETIME(6)  NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS t_product (
  id         BIGINT         NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  name       VARCHAR(128)   NOT NULL COMMENT '商品名称',
  price      DECIMAL(10,2)  NOT NULL COMMENT '售价(元)',
  stock      INT            NOT NULL COMMENT '库存快照(实时以Redis为准)',
  enabled    BIT(1)         NOT NULL COMMENT '是否上架: 1上架 0下架',
  created_at DATETIME(6)    NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE IF NOT EXISTS t_order (
  id            BIGINT         NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  order_no      VARCHAR(64)    NOT NULL COMMENT '订单号',
  user_id       BIGINT         NOT NULL COMMENT '下单用户ID',
  product_id    BIGINT         NOT NULL COMMENT '商品ID',
  product_name  VARCHAR(128)   NOT NULL COMMENT '商品名称快照',
  quantity      INT            NOT NULL COMMENT '购买数量',
  unit_price    DECIMAL(10,2)  NOT NULL COMMENT '下单单价',
  total_amount  DECIMAL(12,2)  NOT NULL COMMENT '订单总金额',
  status        VARCHAR(16)    NOT NULL COMMENT '状态: CREATED/PAID/CANCELLED',
  created_at    DATETIME(6)    NOT NULL COMMENT '下单时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_order_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 文件路径：src/test/resources/schema.sql
-- H2 测试数据库初始化脚本（H2 兼容语法）
-- Spring Boot 会在 @DataJpaTest 启动时自动执行

-- ============================================
-- 删除已存在的表（避免冲突）
-- ============================================
DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_product;
DROP TABLE IF EXISTS t_user;

-- ============================================
-- 用户表 (H2 兼容版本)
-- ============================================
CREATE TABLE t_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  nickname VARCHAR(50),
  phone VARCHAR(20),
  email VARCHAR(100),
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_username UNIQUE (username)
);

-- 为 t_user 创建索引
CREATE INDEX idx_phone ON t_user(phone);

-- ============================================
-- 商品表 (H2 兼容版本)
-- ============================================
CREATE TABLE t_product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_name VARCHAR(200) NOT NULL,
  product_code VARCHAR(50) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  category VARCHAR(50),
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_product_code UNIQUE (product_code)
);

-- 为 t_product 创建索引
CREATE INDEX idx_category ON t_product(category);

-- ============================================
-- 订单表 (H2 兼容版本)
-- ============================================
CREATE TABLE t_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(32) NOT NULL,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status TINYINT NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- 唯一约束
  CONSTRAINT uk_order_no UNIQUE (order_no),

  -- 外键约束
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES t_user(id) ON DELETE RESTRICT,
  CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES t_product(id) ON DELETE RESTRICT
);

-- 为 t_order 创建索引
CREATE INDEX idx_user_id ON t_order(user_id);
CREATE INDEX idx_product_id ON t_order(product_id);
CREATE INDEX idx_status ON t_order(status);
CREATE INDEX idx_create_time ON t_order(create_time);
CREATE INDEX idx_total_amount ON t_order(total_amount);

-- 复合索引
CREATE INDEX idx_user_status_time ON t_order(user_id, status, create_time);
CREATE INDEX idx_create_time_status ON t_order(create_time, status);
CREATE INDEX idx_product_status_time ON t_order(product_id, status, create_time);
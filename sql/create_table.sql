-- 创建库
create database if not exists meeting;

-- 切换库
use meeting;

-- 用户表
create table if not exists `users` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码(加密存储)',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0:正常,1:禁用)',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引',
    index `login` (`username`, `password`) COMMENT '登陆索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
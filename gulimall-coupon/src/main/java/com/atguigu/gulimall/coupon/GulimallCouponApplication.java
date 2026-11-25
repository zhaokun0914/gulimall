package com.atguigu.gulimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 优惠券服务启动类。
 *
 * 启动 gulimall-coupon 微服务，扫描优惠券模块的 DAO 接口，
 * 并将服务注册到 Nacos。
 */
@EnableDiscoveryClient // 开启服务注册与发现，将当前服务注册到 Nacos
@MapperScan("com.atguigu.gulimall.coupon.dao") // 扫描 DAO 接口，交由 MyBatis 生成代理实现
@SpringBootApplication // 标记为 Spring Boot 应用，开启自动配置与组件扫描
public class GulimallCouponApplication {

    public static void main(String[] args) {
        // 清除系统 SOCKS 代理，避免本机代理配置影响 Nacos、MySQL 等连接
        System.setProperty("socksProxyHost", "");
        SpringApplication.run(GulimallCouponApplication.class, args);
    }

}


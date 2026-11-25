package com.atguigu.gulimall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 订单服务启动类。
 *
 * 启动 gulimall-order 微服务，使用 Nacos 作为注册与配置中心。
 */
@SpringBootApplication // 标记为 Spring Boot 应用，开启自动配置与组件扫描
public class GulimallOrderApplication {

    public static void main(String[] args) {
        // 清除系统 SOCKS 代理，避免本机代理配置影响 Nacos、MySQL 等连接
        System.setProperty("socksProxyHost", "");
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}

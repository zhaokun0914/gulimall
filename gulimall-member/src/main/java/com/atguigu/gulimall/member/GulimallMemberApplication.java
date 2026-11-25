package com.atguigu.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 会员服务启动类。
 *
 * 启动 gulimall-member 微服务并注册到 Nacos，
 * 同时开启 Feign 客户端扫描，用于调用优惠券等其他服务。
 */
@EnableDiscoveryClient // 开启服务注册与发现，将当前服务注册到 Nacos
@EnableFeignClients(basePackages = "com.atguigu.gulimall.member.feign") // 扫描 Feign 客户端接口，开启远程调用
@SpringBootApplication // 标记为 Spring Boot 应用，开启自动配置与组件扫描
public class GulimallMemberApplication {

    public static void main(String[] args) {
        // 清除系统 SOCKS 代理，避免本机代理配置影响 Nacos、MySQL 等连接
        System.setProperty("socksProxyHost", "");
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}

package com.atguigu.gulimall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 第三方服务启动类。
 *
 * 启动 gulimall-third-party 微服务，提供 OSS 等第三方能力；
 * 该服务不连接数据库。
 */
@EnableDiscoveryClient // 开启服务注册与发现，将当前服务注册到 Nacos
@SpringBootApplication // 标记为 Spring Boot 应用，开启自动配置与组件扫描
public class GulimallThirdPartyApplication {

    public static void main(String[] args) {
        // 清除系统 SOCKS 代理，避免本机代理配置影响 Nacos、OSS 等连接
        System.setProperty("socksProxyHost", "");
        SpringApplication.run(GulimallThirdPartyApplication.class, args);
    }

}

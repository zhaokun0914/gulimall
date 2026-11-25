package com.atguigu.gulimall.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API 网关启动类。
 *
 * 启动 gulimall-getway 网关，统一处理路由与跨域请求；
 * 网关不连接数据库，因此排除 DataSource 自动配置。
 */
@EnableDiscoveryClient // 注册到 Nacos，配合 lb:// 路由转发到各微服务
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) // 无数据库，排除数据源自动配置，避免启动失败
public class GulimallGetwayApplication {

    public static void main(String[] args) {
        // 清除系统 SOCKS 代理，避免本机代理配置影响 Nacos 等连接
        System.setProperty("socksProxyHost", "");
        SpringApplication.run(GulimallGetwayApplication.class, args);
    }

}

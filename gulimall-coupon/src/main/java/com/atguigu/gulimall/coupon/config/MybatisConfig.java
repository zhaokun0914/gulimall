package com.atguigu.gulimall.coupon.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 请求超过最大页时回到第一页
        paginationInterceptor.setOverflow(true);
        // 单页最多查询 1000 条
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }
}

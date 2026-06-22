package com.atguigu.gulimall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GulimallWareApplication {

	public static void main(String[] args) {
		System.setProperty("socksProxyHost", "");
		SpringApplication.run(GulimallWareApplication.class, args);
	}

}

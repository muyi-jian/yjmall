package com.yang.yjmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.yang.yjmall.product.dao")
@SpringBootApplication
public class YjmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallProductApplication.class, args);
    }

}

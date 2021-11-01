package com.yang.yjmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 1.开启服务注册发现
 *    配置nacos的注册中心地址
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class YjmallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallGatewayApplication.class, args);
    }

}

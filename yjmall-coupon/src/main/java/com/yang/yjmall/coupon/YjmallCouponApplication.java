package com.yang.yjmall.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YjmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallCouponApplication.class, args);
    }

}

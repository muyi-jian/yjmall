package com.yang.yjmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1.逻辑删除
 *  1).全局的逻辑删除规则（可省略）
 *  2).配置逻辑删除的组件（3.1.1开始不在需要这一步）
 *  3).实体类字段上加上注解@TbaleLogic 代表逻辑删除字段
 */
@EnableDiscoveryClient
@MapperScan("com.yang.yjmall.product.dao")
@SpringBootApplication
public class YjmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallProductApplication.class, args);
    }

}

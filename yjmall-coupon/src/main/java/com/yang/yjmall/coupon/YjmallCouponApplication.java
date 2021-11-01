package com.yang.yjmall.coupon;

import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.alibaba.nacos.api.annotation.NacosProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 1.如何使用nacos作为配置中心同意管理配置
 * 1).引入依赖
 *         <dependency>
 *             <groupId>com.alibaba.cloud</groupId>
 *             <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
 *         </dependency>
 * 2).创建爱你一个bootstrap.properties.
 *      spring.application.name=yjmall-coupon
 *      spring.cloud.nacos.config.server-addr=192.168.56.10:8848
 * 3).需要配置中心默认添加一个叫数据集（Data Id） yjmall-coupon.properties.默认规则：应用名.properties
 * 4).给应用名.properties添加任何配置
 * 5).动态获取配置
 * @refreshScope 动态获取并刷新配置
 * @Valus("${配置项的名}") 获取到配置
 *
 * 如果配置中心和当前应用的配置文件中都配置了相同的项。优先获取配置中心的配置
 *2.细节
 * 1).命名空间：配置隔离
 *  默认：public（保留空间）：默认新增的所有配置都在public空间
 *  1.开发、测试、生产：利用命名空间来做环境隔离
 *  注意：在bootstrap.properties配置上，需要使用那个命名空间下的配置，
 *      spring.cloud.nacos.config.namespace=xxxxxxxxxxxxxxxxxx
 *  2.每一个微服务之间相互隔离，每一微服务都创建自己的命名空间，只加载自己命名空间下的所有配置
 *
 * 2)配置集:所有配置的集合
 *
 * 3)配置集ID：类似文件名
 *  Data Id:类似文件名
 *
 * 4)配置分组
 *  默认所有的配置集都属于：DEFAULT_GROUP
 *
 *
 *每个微服务创建自己的命名空间，使用配置分组区分环境，dev,test,prod
 */
@EnableDiscoveryClient
@SpringBootApplication
public class YjmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallCouponApplication.class, args);
    }

}

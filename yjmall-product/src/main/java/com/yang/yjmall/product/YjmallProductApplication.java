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
 * 2.JSR303
 *  1).添加校验注解javax.validation.constraints
 *  2).开启校验功能@Valid
 *      效果：校验错误以后会有默认的响应
*   3).给校验的bean后紧跟一个BinddingResult,就可以获取到校验的结果
 *  4).分组校验（多场景的复杂校验）
 *    1)、@NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *        给校验注解标注什么情况需要进行校验
 *    2）、@Validated({AddGroup.class})
 *    3)、默认没有指定分组的校验注解@NotBlank，在分组校验情况@Validated({AddGroup.class})下不生效，只会在@Validated生效；
 * 3、自定义校验
 *    1）、编写一个自定义的校验注解
 *    2）、编写一个自定义的校验器 ConstraintValidator
 *    3）、关联自定义的校验器和自定义的校验注解
 *      *@Documented
 *      *@Constraint(validatedBy = { ListValueConstraintValidator.class【可以指定多个不同的校验器，适配不同类型的校验】 })
 *      *@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 *      *@Retention(RUNTIME)
 *      public @interface ListValue {
 * 4.统一异常处理
 *  1)编写异常处理类，使用@ControllerAdvice
 *  2)使用@ExceptionHandler标注方法可以处理的异常
 *  3)
 */
@EnableDiscoveryClient
@MapperScan("com.yang.yjmall.product.dao")
@SpringBootApplication
public class YjmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(YjmallProductApplication.class, args);
    }

}

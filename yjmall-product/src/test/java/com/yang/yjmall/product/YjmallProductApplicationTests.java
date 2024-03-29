package com.yang.yjmall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.yjmall.product.entity.BrandEntity;
import com.yang.yjmall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class YjmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        /*brandEntity.setName("华为");
        brandService.save(brandEntity);*/
        /*brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
        brandService.updateById(brandEntity);*/

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::println);


    }

}

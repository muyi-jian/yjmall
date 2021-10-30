package com.yang.yjmall.member.feign;

import com.yang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @PackageName: com.yang.yjmall.member.feign
 * @ClassName: CouponFeignService
 * @Description:
 * @Author: yangjian
 * @Date: 2021/10/30 19:08
 * @Version: 1.0
 */

@FeignClient("yjmall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupon();
}

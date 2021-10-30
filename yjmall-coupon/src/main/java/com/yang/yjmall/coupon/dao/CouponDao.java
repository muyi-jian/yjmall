package com.yang.yjmall.coupon.dao;

import com.yang.yjmall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author yangjian
 * @email yangjian2628168@163.com
 * @date 2021-10-30 18:57:32
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}

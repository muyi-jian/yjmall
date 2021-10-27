package com.yang.yjmall.order.dao;

import com.yang.yjmall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author yangjian
 * @email yangjian2628168@163.com
 * @date 2021-10-27 22:12:41
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}

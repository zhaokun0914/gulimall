package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 20:57:46
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}

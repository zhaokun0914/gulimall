package com.atguigu.gulimall.coupon.dao;

import com.atguigu.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 20:30:41
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}

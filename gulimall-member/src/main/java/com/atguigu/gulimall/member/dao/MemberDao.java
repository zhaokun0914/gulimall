package com.atguigu.gulimall.member.dao;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 20:37:52
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}

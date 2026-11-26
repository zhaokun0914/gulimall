package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 * 
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 15:20:59
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * 分类名变化时同步更新关联表中的分类名
     */
    void updateCategory(@Param("catId") Long catId, @Param("name") String name);
}

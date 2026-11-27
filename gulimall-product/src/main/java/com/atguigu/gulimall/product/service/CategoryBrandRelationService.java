package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 15:20:59
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存品牌与分类的关联关系（同时冗余品牌名、分类名）
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 品牌名变化时同步更新关联表中的冗余字段
     */
    void updateBrand(Long brandId, String name);

    /**
     * 分类名变化时同步更新关联表中的冗余字段
     */
    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}


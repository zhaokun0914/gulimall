package com.atguigu.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author Kavin
 * @email Kavin@gmail.com
 * @date 2026-06-09 15:20:59
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存规格参数，并保存属性与分组的关联关系
     */
    void saveAttr(AttrVo attr);

    /**
     * 分页查询规格参数/销售属性，返回结果带分类名和分组名
     */
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    /**
     * 查询属性详情，返回分类路径和所属分组
     */
    AttrRespVo getAttrInfo(Long attrId);

    /**
     * 修改属性，并同步修改属性与分组的关联关系
     */
    void updateAttr(AttrVo attr);

    /**
     * 查询属性分组已关联的属性
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    /**
     * 删除属性与分组的关联关系
     */
    void deleteRelation(AttrGroupRelationVo[] vos);

    /**
     * 查询属性分组可以关联的属性（同分类下还未被其他分组关联的基本属性）
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}


package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * 规格参数新增/修改的请求对象
 * 相比 AttrEntity 多了一个 attrGroupId，用于同时保存属性与分组的关系
 */
@Data
public class AttrVo {

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 值类型[0-为单个值，1-可以选择多个值]
     */
    private Integer valueType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用分号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0-禁用，1-启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示[0-否，1-是]
     */
    private Integer showDesc;
    /**
     * 所属分组id
     */
    private Long attrGroupId;

}

package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * 属性与属性分组关联的请求对象
 * 新增关联时传递 attrId 与 attrGroupId
 */
@Data
public class AttrGroupRelationVo {

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性分组id
     */
    private Long attrGroupId;

}

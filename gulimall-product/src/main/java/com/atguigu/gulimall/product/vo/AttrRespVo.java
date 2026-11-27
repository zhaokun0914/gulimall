package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * 规格参数列表/详情返回对象
 * 在原属性信息基础上补充分类名、分组名和分类路径
 */
@Data
public class AttrRespVo extends AttrVo {

    /**
     * 所属分类名字
     */
    private String catelogName;
    /**
     * 所属分组名字
     */
    private String groupName;
    /**
     * 分类完整路径（父-子-孙）
     */
    private Long[] catelogPath;

}

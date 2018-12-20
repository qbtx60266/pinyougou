package com.pinyougou.search.service;

import java.util.List;
import java.util.Map;

/**
 * @author FallingSkies
 * @date 2018/12/17 16:19
 */
public interface ItemSearchService {
    /**
     * 搜索方法
     * @param searchMap
     * @return
     */
    public Map search(Map searchMap);


    /**
     * 导入列表
     * @param list
     */
    void importList(List list);

    /**
     * 删除商品列表索引
     * @param goodsIds SPU
     */
    void deleteByGoodsIds(List goodsIds);
}

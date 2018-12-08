package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {
    List<TbBrand> findAll();

    /**
     *
     * @param pageNum 当前页
     * @param pageSize 每页记录
     * @return
     */
    PageResult findPage(Integer pageNum,Integer pageSize);

}

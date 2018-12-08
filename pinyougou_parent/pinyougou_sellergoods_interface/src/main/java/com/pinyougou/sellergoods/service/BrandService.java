package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;

/**
 * 品牌接口
 */
public interface BrandService {
    /**
     * 查询所有
     * @return
     */
    List<TbBrand> findAll();

    /**
     *  分页查询品牌
     * @param pageNum 当前页
     * @param pageSize 每页记录
     * @return
     */
    PageResult findPage(Integer pageNum,Integer pageSize);

    /**
     * 添加品牌
     * @param tbBrand 品牌
     */
    void add(TbBrand tbBrand);

}

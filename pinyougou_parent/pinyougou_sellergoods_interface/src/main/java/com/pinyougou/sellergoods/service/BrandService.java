package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 品牌接口
 */
public interface BrandService {
    /**
     * 查询所有
     *
     * @return
     */
    List<TbBrand> findAll();

    /**
     * 分页查询品牌
     *
     * @param pageNum  当前页
     * @param pageSize 每页记录
     * @return
     */
    PageResult findPage(Integer pageNum, Integer pageSize);

    /**
     * 添加品牌
     *
     * @param tbBrand 品牌
     */
    void add(TbBrand tbBrand);

    /**
     * 根据id查询品牌
     *
     * @param id
     * @return
     */
    TbBrand findOne(Long id);

    /**
     * 修改品牌
     *
     * @param tbBrand
     */
    void update(TbBrand tbBrand);

    /**
     * 删除品牌
     *
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 分页模糊查询
     *
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(TbBrand brand, Integer pageNum, Integer pageSize);

    /**
     * 返回下拉列表数据
     * @return
     */
    List<Map> selectOptionList();
}

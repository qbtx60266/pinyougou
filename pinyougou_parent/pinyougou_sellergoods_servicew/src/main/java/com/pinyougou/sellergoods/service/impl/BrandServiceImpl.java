package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 品牌实现类
 * @author FallingSkies
 * @date 2018/12/6 10:11
 */
@Service(timeout = 10000)
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param rows 每页记录
     * @return
     */
    @Override
    public PageResult findPage(Integer page,Integer rows) {
        PageHelper.startPage(page,rows);
        Page<TbBrand> trBrandPage = (Page<TbBrand>) brandMapper.selectByExample(null);
        return new PageResult(trBrandPage.getTotal(),trBrandPage.getResult());
    }

    /**
     * 添加品牌
     * @param tbBrand 品牌
     */
    @Override
    public void add(TbBrand tbBrand) {
        TbBrandExample tbBrandExample = new TbBrandExample();

        tbBrandExample.createCriteria().andNameEqualTo(tbBrand.getName());

        List<TbBrand> tbBrands = brandMapper.selectByExample(tbBrandExample);

        if (tbBrands!=null || tbBrands.size()> 0){
            throw new RuntimeException("品牌名重复");
        }

        brandMapper.insert(tbBrand);
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByPrimaryKey(tbBrand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }
    }


}

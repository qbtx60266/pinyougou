package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FallingSkies
 * @date 2018/12/6 10:18
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
     * 分页查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestParam(name = "page",defaultValue = "1") Integer page,
                               @RequestParam(name = "rows",defaultValue = "10") Integer rows){
        return brandService.findPage(page,rows);
    }

    /**
     * 新增方法
     * @param tbBrand
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody TbBrand tbBrand){
        if (tbBrand.getId()==null){
            try {
                brandService.add(tbBrand);
                return new Result(true,"添加成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"添加失败");
            }
        }else {
            try {
                brandService.update(tbBrand);
                return new Result(true,"修改成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"修改失败");
            }
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }


}

package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 搜索控制层
 * @author FallingSkies
 * @date 2018/12/17 17:17
 */

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    /**
     * 搜索方法
     * @param searchMap
     * @return
     */
    @RequestMapping("/search")
    public Map search(@RequestBody Map searchMap){
        return itemSearchService.search(searchMap);
    }
}

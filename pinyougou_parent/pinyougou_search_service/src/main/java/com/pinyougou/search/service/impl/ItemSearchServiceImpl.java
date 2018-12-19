package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FallingSkies
 * @date 2018/12/17 16:45
 */
@Service(timeout = 10000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map search(Map searchMap) {

        Map map = new HashMap();
//        Query query = new SimpleQuery("*:*");
//        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
//        query.addCriteria(criteria);
//        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
//        map.put("rows",page.getContent());
        //高亮显示

        //查询列表
        map.putAll(searchList(searchMap));


        //分组查询商品分类列表
        List<String> categoryList = searchCategory(searchMap);
        map.put("categoryList",categoryList);

        //查询品牌和规格列表
        String category = (String) searchMap.get("category");
        if (!"".equals(category)){
            map.putAll(searchBrandAndSpecList(category));
        }else {
            if (categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }


        return map;
    }

    /**
     * 实现高亮
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map = new HashMap();
        //高亮选项初始化
        HighlightQuery query = new SimpleHighlightQuery();
        //高亮域，构建高亮选项对象
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        //设置前后缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        highlightOptions.setSimplePostfix("</em>");
        //为查询对象设置高亮选项
        query.setHighlightOptions(highlightOptions);

        //关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //按照商品分类过滤
        if (!"".equals(searchMap.get("category"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_category").is(searchMap.get("category"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按照品牌过滤
        if (!"".equals(searchMap.get("brand"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));
            filterQuery.addCriteria(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        //按规格过滤
        if (searchMap.get("spec")!=null){
            Map<String,String> specMap = (Map<String, String>) searchMap.get("spec");
            for (String key : specMap.keySet()) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(specMap.get(key));
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }




        //按价格过滤
        if (!"".equals(searchMap.get("price"))){
            String[] price = ((String) searchMap.get("price")).split("-");
            if (!price[0].equals("0")){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").greaterThanEqual(price[0]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
            if (!price[1].equals("*")){
                FilterQuery filterQuery = new SimpleFilterQuery();
                Criteria filterCriteria = new Criteria("item_price").lessThanEqual(price[1]);
                filterQuery.addCriteria(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }






        //***************获取高亮结果集**************************
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //高亮入口集合
        List<HighlightEntry<TbItem>> entryList = page.getHighlighted();

        for (HighlightEntry<TbItem> entry : entryList) {
            //获取高亮列表
            List<HighlightEntry.Highlight> highlightList = entry.getHighlights();
            /*for (HighlightEntry.Highlight h : highlightList) {
                List<String> sns = h.getSnipplets();
            }*/

            if (highlightList.size()>0 && highlightList.get(0).getSnipplets().size()>0){
                TbItem item = entry.getEntity();
                item.setTitle(highlightList.get(0).getSnipplets().get(0));
            }
        }

        map.put("rows",page.getContent());
        return map;
    }

    /**
     * 分组查询(查询商品分类列表)
     * @return
     */
    private List<String> searchCategory(Map searchMap){
        List<String> list = new ArrayList<>();

        Query query = new SimpleQuery("*:*");
        //等效于关键字查询where
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //group by设置分组选项
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //获取分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);

        //获取分组结果对象
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");

        //获取分组入口页
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();

        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

        for (GroupEntry<TbItem> entry : entryList) {
            //将分组的结果添加到返回值中
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * 查询品牌和规格列表
     * @param category 商品分类名称
     * @return
     */
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        //根据商品分类名称得到模板ID
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (templateId != null){
            //根据模板ID获取品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList",brandList);
            //根据模板ID获取规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList",specList);
        }
        return map;
    }
}

package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author FallingSkies
 * @date 2018/12/17 14:51
 */
@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private SolrTemplate solrTemplate;


    public void importItemData(){
        TbItemExample example = new TbItemExample();
        //审核通过
        example.createCriteria().andStatusEqualTo("1");
        List<TbItem> itemList = tbItemMapper.selectByExample(example);
        System.out.println("商品列表");
        for (TbItem item : itemList) {
            System.out.println(item.getTitle());
            System.out.println("============================");
            Map map = JSON.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(map);

        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
        System.out.println("end");
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) applicationContext.getBean("solrUtil");
        solrUtil.importItemData();
    }
}

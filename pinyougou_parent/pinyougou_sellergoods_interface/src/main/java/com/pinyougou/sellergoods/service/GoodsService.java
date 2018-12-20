package com.pinyougou.sellergoods.service;
import java.util.List;
import com.pinyougou.pojo.TbGoods;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojogroup.Goods;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(Goods goods);
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 运营商批量修改审核状态
	 * @param ids
	 * @param status
	 */
	void updateStatus(Long[] ids,String status);

	/**
	 * 商家批量修改上下架状态
	 * @param ids
	 * @param marketable
	 */
	void updateMarketable(Long[] ids,String marketable);

	/**
	 * 根据SPU ID集合查询SKU列表
	 * @param goodsIds
	 * @param status
	 * @return
	 */
	List<TbItem> findItemListByGoodsIdListAndStatus(Long[] goodsIds,String status);
}

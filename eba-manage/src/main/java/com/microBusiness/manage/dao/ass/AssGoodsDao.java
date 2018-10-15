package com.microBusiness.manage.dao.ass;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;

public interface AssGoodsDao extends BaseDao<AssGoods, Long> {
	
	boolean snExists(String sn);
	
	Page<AssGoods> findPage(AssChildMember assChildMember , Pageable pageable);
	
	Page<AssGoods> findByList(AssCustomerRelation assCustomerRelation , String name , Pageable pageable);
	
	/**
	 * 根据供应商和商品名称查询是否有重复的名称
	 */
	boolean findNameExist(AssCustomerRelation assCustomerRelation , AssGoods assGoods);
	
	List<AssGoods> findByList(AssCustomerRelation assCustomerRelation);
	
	/**
	 * 根据供应商和商品名称查询商品
	 */
	AssGoods findNameExist(AssCustomerRelation assCustomerRelation , String name);

	AssGoods findBySn(String sn);
	
	AssGoods findBySource(AssGoods assGoods,AssChildMember assChildMember);
	
	AssGoods find(Goods goods , AssCustomerRelation assCustomerRelation);
	
	List<Goods> find(AssCustomerRelation assCustomerRelation);
}

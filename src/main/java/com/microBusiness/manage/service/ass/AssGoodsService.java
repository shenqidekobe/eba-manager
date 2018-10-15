package com.microBusiness.manage.service.ass;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
import com.microBusiness.manage.service.BaseService;

public interface AssGoodsService extends BaseService<AssGoods, Long> {
	
	Page<AssGoods> findPage(AssChildMember assChildMember , Pageable pageable);
	
	AssGoods save(AssGoods assGoods , AssCustomerRelation assCustomerRelation, List<String> specification);
	
	AssGoods save(AssGoods assGoods , AssProduct assProduct , AssCustomerRelation assCustomerRelation);
	
	boolean deleteAssGoods(Long id);
	
	AssGoods updateAssGoods(AssGoods assGoods, List<AssProduct> assProducts,  AssCustomerRelation assCustomerRelation);
	
	List<AssProduct> getSpecifications(Long id);
	
	Page<AssGoods> findByList(AssCustomerRelation assCustomerRelation , String name , Pageable pageable);
	
	/**
	 * 根据供应商和商品名称查询是否有重复的名称
	 */
	boolean findNameExist(AssCustomerRelation assCustomerRelation , AssGoods assGoods);
	
	/**
	 * 复制分享商品
	 * @param assCustomerRelation
	 * @param assChildMember
	 * @param synchronizationFla
	 * @return
	 */
	AssCustomerRelation copyShareAssGoods(AssCustomerRelation assCustomerRelation , AssChildMember assChildMember ,String sn, Boolean synchronizationFla);
	
	/**
	 * 复制未分享商品
	 * @param assCustomerRelation
	 * @param assChildMember
	 * @param synchronizationFla
	 * @return
	 */
	AssCustomerRelation copyNoShareAssGoods(AssCustomerRelation assCustomerRelation , AssChildMember assChildMember , String mClientName , String mTheme);
	
	AssCustomerRelation copyToNoShareAssGoods(AssCustomerRelation assCustomerRelation , AssChildMember assChildMember);
	
	BufferedImage getPoster(AssGoods assGoods,HttpServletRequest request, HttpServletResponse response) throws IOException;
	/**
	 * 商品列表复制单个商品
	 * @param assCustomerRelation 
	 * @param assGoods
	 * @return
	 */
	AssGoods copyGoods(AssCustomerRelation assCustomerRelation , AssGoods assGoods);
	
	List<AssGoods> findByList(AssCustomerRelation assCustomerRelation);
	
	/**
	 * 复制单个分享商品
	 * @param assCustomerRelation 要复制的供应商信息
	 * @param assGoods 要复制的商品信息
	 * @param assChildMember
	 * @return
	 */
	AssGoods copySingleShareAssAssGoods(AssCustomerRelation assCustomerRelation , AssGoods assGoods ,String sn);
	
	/**
	 * 把单个分享商品复制为为分享商品
	 * @param assCustomerRelation 要复制的供应商信息
	 * @param assGoods 要复制的商品信息
	 * @param assChildMember
	 * @return
	 */
	AssGoods copySingleNoShareAssAssGoods(AssCustomerRelation assCustomerRelation , AssGoods assGoods , AssChildMember assChildMember);
	
	AssGoods findBySn(String sn);
	
	AssGoods findBySource(AssGoods assGoods,AssChildMember assChildMember);
	
}

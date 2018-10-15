/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.StockGoodsDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;
import com.microBusiness.manage.entity.StockGoods;
import com.microBusiness.manage.entity.StockGoods.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

@Repository("stockGoodsDaoImpl")
public class StockGoodsDaoImpl extends BaseDaoImpl<StockGoods, Long> implements StockGoodsDao {

	@Override
	public StockGoods findByProduct(Product product, Shop shop) {
		try {
			String jpql = "select stockGoods from StockGoods stockGoods where stockGoods.product=:product and stockGoods.shop=:shop";
			return entityManager.createQuery(jpql, StockGoods.class).setParameter("product", product).setParameter("shop", shop).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Page<StockGoods> page(Shop shop, String Keyword, Status status, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StockGoods> criteriaQuery = criteriaBuilder.createQuery(StockGoods.class);
		Root<StockGoods> root = criteriaQuery.from(StockGoods.class);
		Join<StockGoods, Product> ProJoin = root.join("product" , JoinType.LEFT);
		Join<Product, Goods> goodsJoin = ProJoin.join("goods" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(!StringUtils.isEmpty(Keyword)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(goodsJoin.<String>get("name"), "%"+Keyword+"%"));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<StockGoods> findByBarCode(String barCode, Shop shop, Status status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StockGoods> criteriaQuery = criteriaBuilder.createQuery(StockGoods.class);
		Root<StockGoods> root = criteriaQuery.from(StockGoods.class);
		Join<StockGoods, Product> ProJoin = root.join("product" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (!StringUtils.isEmpty(barCode)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(ProJoin.get("barCode"), barCode));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		criteriaQuery.where(restrictions);
		
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<StockGoods> getStockGoodsByMember(Product product, Member member, Shop shop) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StockGoods> criteriaQuery = criteriaBuilder.createQuery(StockGoods.class);
		Root<StockGoods> root = criteriaQuery.from(StockGoods.class);
		Join<StockGoods, Shop> shopJoin = root.join("shop" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (product != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(shopJoin.get("member"), member));
		}
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		}
		criteriaQuery.where(restrictions);
		
		return super.findList(criteriaQuery, null, null, null, null);
	}

}
/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.GoodsDao;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

@Repository("goodsDaoImpl")
public class GoodsDaoImpl extends BaseDaoImpl<Goods, Long> implements GoodsDao {

	public boolean snExists(String sn, Supplier supplier) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String jpql = "select count(*) from Goods goods where lower(goods.sn) = lower(:sn) and goods.supplier =:supplier and goods.deleted=0";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).setParameter("supplier", supplier).getSingleResult();
		return count > 0;
	}

	public Goods findBySn(String sn, Supplier supplier) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}

		String jpql = "select goods from Goods goods where lower(goods.sn) = lower(:sn) and goods.supplier =:supplier and goods.deleted=0";
		try {
			return entityManager.createQuery(jpql, Goods.class).setParameter("sn", sn).setParameter("supplier", supplier).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Goods> findList(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
                                Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
		if (promotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("promotions"), promotion));
		}
		if (tag != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("tags"), tag));
		}
		if (attributeValueMap != null) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			if (isOutOfStock) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, allocatedStock));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isStockAlert != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (hasPromotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("promotions")));
		}
		criteriaQuery.where(restrictions);
		if (orderType != null) {
			switch (orderType) {
			case topDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case priceAsc:
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case priceDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case salesDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case scoreDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("score")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case dateDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
				break;
			}
		} else if (CollectionUtils.isEmpty(orders)) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
		}
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	public List<Goods> findList(ProductCategory productCategory, Boolean isMarketable, Goods.GenerateMethod generateMethod, Date beginDate, Date endDate, Integer first, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (generateMethod != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("generateMethod"), generateMethod));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, first, count, null, null);
	}

	public Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
			Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
		if (promotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("promotions"), promotion));
		}
		if (tag != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("tags"), tag));
		}
		if (attributeValueMap != null) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			if (isOutOfStock) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, allocatedStock));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isStockAlert != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (hasPromotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("promotions")));
		}
		criteriaQuery.where(restrictions);
		if (orderType != null) {
			switch (orderType) {
			case topDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case priceAsc:
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case priceDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case salesDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case scoreDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("score")), criteriaBuilder.desc(root.get("createDate")));
				break;
			case dateDesc:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
				break;
			}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Goods> findPage(Goods.RankingType rankingType, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		if (rankingType != null) {
			switch (rankingType) {
			case score:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("score")), criteriaBuilder.desc(root.get("scoreCount")));
				break;
			case scoreCount:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("scoreCount")), criteriaBuilder.desc(root.get("score")));
				break;
			case weekHits:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("weekHits")));
				break;
			case monthHits:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("monthHits")));
				break;
			case hits:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("hits")));
				break;
			case weekSales:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("weekSales")));
				break;
			case monthSales:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("monthSales")));
				break;
			case sales:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")));
				break;
			}
		}
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Goods> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return Page.emptyPage(pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));
		return super.findPage(criteriaQuery, pageable);
	}

	public Long count(Goods.Type type, Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (favoriteMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteMembers"), favoriteMember));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			if (isOutOfStock) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, allocatedStock));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isStockAlert != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public void clearAttributeValue(Attribute attribute) {
		if (attribute == null || attribute.getPropertyIndex() == null || attribute.getProductCategory() == null) {
			return;
		}

		String jpql = "update Goods goods set goods." + Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex() + " = null where goods.productCategory = :productCategory";
		entityManager.createQuery(jpql).setParameter("productCategory", attribute.getProductCategory()).executeUpdate();
	}


	public Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
								Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable , Supplier supplier, Boolean sourceFlag, Boolean branchFlag) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		Join<Goods , SupplierSupplier> goodSupplierJoin = root.join("supplierSupplier" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		
		if (null != sourceFlag) {
			if (sourceFlag) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("source")));
			}
		}
		
		if (null != branchFlag) {
			if (branchFlag) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(goodSupplierJoin.get("status"), SupplierSupplier.Status.inTheSupply), criteriaBuilder.isNull(goodSupplierJoin.get("status"))));
			}
		}
		
		if (null != supplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}

		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
		if (promotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("promotions"), promotion));
		}
		if (tag != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("tags"), tag));
		}
		if (attributeValueMap != null) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			if (isOutOfStock) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, allocatedStock));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isStockAlert != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			Setting setting = SystemUtils.getSetting();
			if (isStockAlert) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (hasPromotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("promotions")));
		}
		criteriaQuery.where(restrictions);
		if (orderType != null) {
			switch (orderType) {
				case topDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
					break;
				case priceAsc:
					criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
					break;
				case priceDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
					break;
				case salesDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")), criteriaBuilder.desc(root.get("createDate")));
					break;
				case scoreDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(root.get("score")), criteriaBuilder.desc(root.get("createDate")));
					break;
				case dateDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
					break;
			}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	/*public Page<Goods> findPage(Goods.Type type, ProductCategory productCategory, Brand brand, Promotion promotion, Tag tag, Map<Attribute, String> attributeValueMap, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isOutOfStock,
	Boolean isStockAlert, Boolean hasPromotion, Goods.OrderType orderType, Pageable pageable , Supplier supplier, Boolean sourceFlag, Boolean branchFlag) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		Join<Goods , SupplierSupplier> goodSupplierJoin = root.join("supplierSupplier" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (type != null) {
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
		}
		if (productCategory != null) {
		Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
		Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
		subquery.select(subqueryRoot);
		subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		
		if (null != sourceFlag) {
			if (sourceFlag) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("source")));
			}
		}
		
		if (null != branchFlag) {
			if (branchFlag) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(goodSupplierJoin.get("status"), SupplierSupplier.Status.inTheSupply), criteriaBuilder.isNull(root.get("source"))));
			}
		}
		
		if (null != supplier) {
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (brand != null) {
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
		if (promotion != null) {
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("promotions"), promotion));
		}
		if (tag != null) {
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("tags"), tag));
		}
		if (attributeValueMap != null) {
		for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
			String propertyName = Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
		Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
		Root<Product> subqueryRoot = subquery.from(Product.class);
		subquery.select(subqueryRoot);
		Path<Integer> stock = subqueryRoot.get("stock");
		Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
		if (isOutOfStock) {
			subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
		} else {
			subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, allocatedStock));
		}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isStockAlert != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			Setting setting = SystemUtils.getSetting();
		if (isStockAlert) {
			subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
		} else {
			subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), root), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
		}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (hasPromotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.join("promotions")));
		}
		criteriaQuery.where(restrictions);
		if (orderType != null) {
		switch (orderType) {
			case topDesc:
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
			break;
			case priceAsc:
			criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
			break;
			case priceDesc:
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")), criteriaBuilder.desc(root.get("createDate")));
			break;
			case salesDesc:
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("sales")), criteriaBuilder.desc(root.get("createDate")));
			break;
			case scoreDesc:
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("score")), criteriaBuilder.desc(root.get("createDate")));
			break;
			case dateDesc:
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
			break;
		}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")), criteriaBuilder.desc(root.get("createDate")));
		}
		return super.findPage(criteriaQuery, pageable);
	}*/
	
	@Override
	public Page<Goods> findBySupplierSupplierPage(List<Long> goodIds, Pageable pageable , Long productCategoryId) {
		// FIXME: 2017/5/23 这里容易产生sql注入，需要修复
		String searchProperty=pageable.getSearchProperty();
		String searchValue=pageable.getSearchValue();
		String orderProperty=pageable.getOrderProperty();
		String sql="select goods from Goods goods where goods.id in(:goodIds) ";
		StringBuilder sql1 = new StringBuilder("select count(1) from Goods goods where goods.id in(:goodIds) ");
		if (searchProperty != null && searchValue != null) {
			sql+=" and  goods."+searchProperty+" like '%"+searchValue+"%' ";
			sql1.append(" and goods."+searchProperty+" like '%"+searchValue+"%' ");
		}

		if(null != productCategoryId){
			sql+= " and goods.productCategory.id=:productCategoryId" ;
			sql1.append(" and goods.productCategory.id=:productCategoryId");
		}

		if (orderProperty != null && pageable.getOrderDirection() !=null) {
			sql+=" order by "+orderProperty+" "+pageable.getOrderDirection()+" ";
		}
		TypedQuery<Goods> query=entityManager.createQuery(sql, Goods.class);
		TypedQuery<Long> totalQuery =entityManager.createQuery(sql1.toString(), Long.class);
		query.setFirstResult((pageable.getPageNumber()-1)*pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		query.setParameter("goodIds", goodIds);
		totalQuery.setParameter("goodIds", goodIds);
		if(null != productCategoryId){
			query.setParameter("productCategoryId", productCategoryId);
			totalQuery.setParameter("productCategoryId", productCategoryId);
		}
		long total = totalQuery.getSingleResult().longValue() ;
		Page<Goods> page=new Page<>(query.getResultList(),total,pageable);
		return page;
	}

	/**
	 * 前端获取正式供应列表
	 *
	 * @param needId       收货点id
	 * @param supplierId   供应商id
	 * @param keywords     搜索关键字
	 * @param isMarketable 是否上架
	 * @return
	 */
	@Override
	public List<Goods> getGoodsByTemporary(Long needId, Long supplierId, String keywords, Integer isMarketable , Pageable pageable , Long relationId) {
		/*select distinct goods.id , goods.* from xx_goods goods
		inner join xx_product product on goods.id=product.`goods`
		inner join t_supplier_need_product assProduct on product.id=assProduct.products
		inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation
		where assProduct.need=5 and goods.`is_marketable`=true and supplierRel.supplier=1 and (supplierRel.start_date <= now() <= supplierRel.end_date)*/

		StringBuffer findSql = new StringBuffer("select distinct goods.id , goods.* from xx_goods goods");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supply_need supplierRel on supplierRel.id=assProduct.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(null != needId){
			findSql.append(" and supplierRel.need=:needId");
		}
		if(null != relationId){
			findSql.append(" and supplierRel.id=:relationId");
		}

		if(null != keywords){
			findSql.append(" and goods.name like :goodsName");
		}

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				findSql.append(" and goods.product_category=:searchValue");
			}
		}

		//处理排序
		String orderProperty = pageable.getOrderProperty() ;
		Order.Direction direction = pageable.getOrderDirection();
		if(StringUtils.isNotEmpty(orderProperty) && null != direction){
			String directionStr = "asc";
			switch (direction){
				case asc:
					directionStr = "asc";
					break ;
				case desc:
					directionStr = "desc";
					break ;
			}
			if(orderProperty.equals("sales")){
				findSql.append(" order by goods.sales ").append(directionStr).append(" , goods.id desc");;
			}
		}else{
			findSql.append(" order by goods.create_date desc");
		}



		Query query = entityManager.createNativeQuery(findSql.toString() , Goods.class) ;

		if(null != needId){
			query.setParameter("needId" , needId);
		}
		if(null != relationId){
			query.setParameter("relationId" , relationId) ;
		}

		if(StringUtils.isNotEmpty(keywords)){
			query.setParameter("goodsName", "%" + keywords + "%");
		}

		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				query.setParameter("searchValue", Long.valueOf(searchValue));
			}

		}

		List<Goods> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<Goods> queryDistributionGoods(Supplier supplier,
			Long supplierSupplierId) {
			StringBuffer findSql = new StringBuffer("select * from xx_goods where 1=1 and deleted=0");
			if(supplier.getId() != null) {
				findSql.append(" and supplier=:supplierId");
			}
			if(supplierSupplierId != null) {
				findSql.append(" and supplier_supplier=:supplierSupplierId");
			}
			Query query = entityManager.createNativeQuery(findSql.toString() , Goods.class) ;
			if(supplier.getId() != null) {
				query.setParameter("supplierId" , supplier.getId()) ;
			}
			if(supplierSupplierId != null) {
				query.setParameter("supplierSupplierId" , supplierSupplierId) ;
			}
			return query.getResultList();
	}
	@Override
	public List<Goods> findByProductCategoryId(Supplier supplier,
			ProductCategory productCategory , SupplierSupplier supplierSupplier) {
		StringBuffer findSql = new StringBuffer("select * from xx_goods goods where 1=1");
		if(supplier.getId() != null) {
			findSql.append(" and goods.supplier=:supplier");
		}
		if(productCategory.getId() != null) {
			findSql.append(" and goods.product_category=:productCategory");
		}
		if(supplierSupplier.getId() != null) {
			findSql.append(" and goods.supplier_supplier=:supplierSupplierId");
		}
		Query query = entityManager.createNativeQuery(findSql.toString() , Goods.class) ;
		if(supplier.getId() != null) {
			query.setParameter("supplier" , supplier.getId()) ;
		}
		if(productCategory.getId() != null) {
			query.setParameter("productCategory" , productCategory.getId()) ;
		}
		if(supplierSupplier.getId() != null) {
			query.setParameter("supplierSupplierId" , supplierSupplier.getId()) ;
		}
		return query.getResultList();
	}

	@Override
	public List<Goods> getGoodsByFormal(Long needId, Long supplierId,
			String keywords, Integer isMarketable, Pageable pageable , Long relationId) {
		StringBuffer findSql = new StringBuffer("select distinct goods.* from xx_goods goods");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation");
		findSql.append(" where 1=1 and goods.is_marketable=true ");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());
		
		if(null != needId){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != relationId){
			findSql.append(" and supplierRel.id=:relationId");
		}

		if(null != keywords){
			findSql.append(" and goods.name like :goodsName");
		}

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				findSql.append(" and goods.product_category=:searchValue");
			}
		}

		//处理排序
		String orderProperty = pageable.getOrderProperty() ;
		Order.Direction direction = pageable.getOrderDirection();
		if(StringUtils.isNotEmpty(orderProperty) && null != direction){
			String directionStr = "asc";
			switch (direction){
				case asc:
					directionStr = "asc";
					break ;
				case desc:
					directionStr = "desc";
					break ;
			}
			if(orderProperty.equals("sales")){
				findSql.append(" order by goods.sales ").append(directionStr).append(" , goods.id desc");
			}
		}else{
			findSql.append(" order by goods.create_date desc");
		}



		Query query = entityManager.createNativeQuery(findSql.toString() , Goods.class) ;

		if(null != needId){
			query.setParameter("needId" , needId);
		}
		if(null != relationId){
			query.setParameter("relationId" , relationId) ;
		}

		if(StringUtils.isNotEmpty(keywords)){
			query.setParameter("goodsName", "%" + keywords + "%");
		}

		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("productCategory")){
				query.setParameter("searchValue", Long.valueOf(searchValue));
			}

		}

		List<Goods> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<Goods> findByName(String name, Supplier supplier, Types types) {
		try {
			String jpql = "select goods from Goods goods where goods.name =:name and goods.supplier =:supplier and goods.deleted = 0";
			return entityManager.createQuery(jpql, Goods.class).setParameter("name", name).setParameter("supplier", supplier).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	@Override
	public Page<Goods> findPage(Supplier supplier, String goodName,ProductCategory productCategory, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
		Root<Goods> root = criteriaQuery.from(Goods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("productCategory")).value(subquery));
		}
		
		Join<Goods , SupplierSupplier> goodSupplierJoin = root.join("supplierSupplier" , JoinType.LEFT);
		restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(goodSupplierJoin.isNull(), criteriaBuilder.and(criteriaBuilder.equal(goodSupplierJoin.get("status"), SupplierSupplier.Status.inTheSupply), criteriaBuilder.lessThanOrEqualTo(goodSupplierJoin.<Date> get("startDate"), new Date()),criteriaBuilder.greaterThanOrEqualTo(goodSupplierJoin.<Date> get("endDate"), new Date()))));
		
		if (goodName != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("name") , "%"+goodName+"%"));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<Goods> findPage(AssGoodDirectory assGoodDirectory, String goodName, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select goods.* from xx_goods goods ");
		findSql.append(" LEFT JOIN ass_good_directory_goods directoryGoods on directoryGoods.goods = goods.id ");
		findSql.append(" LEFT JOIN ass_good_directory directory on directory.id = directoryGoods.ass_good_directory ");
		findSql.append(" where 1=1 ");
		if (assGoodDirectory != null) {
			findSql.append(" and directory.id = :id");
		}
		if (StringUtils.isNotBlank(goodName)) {
			findSql.append(" and goods.name like :goodName");
		}
		findSql.append(" ORDER BY goods.create_date DESC");
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");
		
		Query query = entityManager.createNativeQuery(findSql.toString(),Goods.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		
		if (assGoodDirectory != null) {
			query.setParameter("id", assGoodDirectory.getId());
			countQuery.setParameter("id", assGoodDirectory.getId());
		}
		if (StringUtils.isNotBlank(goodName)) {
			query.setParameter("goodName" , "%"+goodName+"%") ;
			countQuery.setParameter("goodName", "%"+goodName+"%");
		}
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		List<Goods> list=query.getResultList();
		
		return new Page<Goods>(list, total, pageable);
		
	}

	@Override
	public void removeDirectoryGoods(Long goodId) {
		String sql=" delete from ass_good_directory_goods where goods = :goodId ";
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("goodId", goodId);
		query.executeUpdate();
	}

	@Override
	public List<Goods> supplyOfGoods(Long id) {
		if(null == id) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT goods.* from xx_goods goods");
			buffer.append(" INNER JOIN xx_product product on goods.id = product.goods");
			buffer.append(" INNER JOIN t_supplier_product supplierProduct on product.id = supplierProduct.products");
			buffer.append(" INNER JOIN t_supplier_supplier supplierSupplier on supplierProduct.supply_relation = supplierSupplier.id where 1=1");
			buffer.append(" and supplierSupplier.status = 1 and goods.id = :id");
			buffer.append(" UNION ALL");
			buffer.append(" select DISTINCT goods1.* from xx_goods goods1");
			buffer.append(" INNER JOIN xx_product product1 on goods1.id = product1.goods");
			buffer.append(" INNER JOIN t_need_product needProduct on product1.id = needProduct.products");
			buffer.append(" INNER JOIN t_supply_need supplyNeed on needProduct.supply_need = supplyNeed.id where 1=1");
			buffer.append(" and supplyNeed.status = 0 and goods1.id = :id");
			Query query = entityManager.createNativeQuery(buffer.toString(), Goods.class);
			query.setParameter("id", id);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Page<Goods> findPage(Supplier supplier,
			ProductCategory productCategory, Pageable pageable , String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
        Root<Goods> root = criteriaQuery.from(Goods.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types"), Types.local));
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        if(null != productCategory) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productCategory"), productCategory));
        }
        if(null != searchName && "" != searchName) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("name"), "%"+ StringUtils.trim(searchName)+"%"));
		}
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery , pageable);
	}

	@Override
	public List<Goods> findLocalGoodsList(Supplier supplier, Types types,
			ProductCategory productCategory, String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Goods> criteriaQuery = criteriaBuilder.createQuery(Goods.class);
        Root<Goods> root = criteriaQuery.from(Goods.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        if(null != types) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types"), types));
        }
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        if(null != productCategory) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productCategory"), supplier));
        }
        if(null != searchName && "" != searchName) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("name"), searchName));
        }
        criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public Page<Goods> findPageByNeedShopProduct(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, Long productCategoryId, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select DISTINCT goods.* from t_need_shop_product needShopProduct ");
		findSql.append(" left join xx_product product on product.id=needShopProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		findSql.append(" where 1=1  and goods.deleted=0 ");
		if (supplyNeed != null) {
			findSql.append(" and needShopProduct.supply_need = :supplyNeed");
		}
		if (shop != null) {
			findSql.append(" and needShopProduct.shop = :shop");
		}
		if (supplier != null) {
			findSql.append(" and needShopProduct.supplier = :supplier");
		}
		if (goodsName != null) {
			findSql.append(" and goods.name like :goodsName ");
		}
		if (productCategoryId != null) {
			findSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
        
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(findSql.toString(),Goods.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (supplyNeed != null) {
			query.setParameter("supplyNeed", supplyNeed.getId());
			countQuery.setParameter("supplyNeed", supplyNeed.getId());
		}
		if (shop != null) {
			query.setParameter("shop", shop.getId());
			countQuery.setParameter("shop", shop.getId());
		}
		if(supplier != null) {
			query.setParameter("supplier", supplier.getId());
			countQuery.setParameter("supplier", supplier.getId());
		}
		if (goodsName != null) {
			query.setParameter("goodsName" , "%" + goodsName + "%");
			countQuery.setParameter("goodsName" , "%" + goodsName + "%");
		}
		if (productCategoryId != null) {
			query.setParameter("categoryId" , productCategoryId);
			countQuery.setParameter("categoryId" , productCategoryId);
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Goods>(query.getResultList(), total, pageable);

	}

	@Override
	public Page<Goods> findPageBySupplierSupplier(SupplierSupplier supplierSupplier, Need need, String goodsName, Long productCategoryId, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select DISTINCT goods.* from t_supplier_need_product supplierNeedProduct ");
		findSql.append(" left join xx_product product on product.id=supplierNeedProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		findSql.append(" where 1=1  and goods.deleted=0 ");
		if (supplierSupplier != null) {
			findSql.append(" and supplierNeedProduct.supply_relation = :supplierSupplier");
		}
		if (need != null) {
			findSql.append(" and supplierNeedProduct.need = :need");
		}
		if (goodsName != null) {
			findSql.append(" and goods.name like :goodsName ");
		}
		if (productCategoryId != null) {
			findSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
        
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(findSql.toString(),Goods.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (supplierSupplier != null) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
			countQuery.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		if (need != null) {
			query.setParameter("need", need.getId());
			countQuery.setParameter("need", need.getId());
		}
		if (goodsName != null) {
			query.setParameter("goodsName" , "%" + goodsName + "%");
			countQuery.setParameter("goodsName" , "%" + goodsName + "%");
		}
		if (productCategoryId != null) {
			query.setParameter("categoryId" , productCategoryId);
			countQuery.setParameter("categoryId" , productCategoryId);
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Goods>(query.getResultList(), total, pageable);
	}

	@Override
	public Page<Goods> findPageBySupplierSupplierGoods(SupplierSupplier supplierSupplier, String goodsName,
			Long productCategoryId, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select DISTINCT goods.* from t_supplier_product supplierProduct ");
		findSql.append(" left join xx_product product on product.id=supplierProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		findSql.append(" where 1=1  and goods.deleted=0 ");
		if (supplierSupplier != null) {
			findSql.append(" and supplierProduct.supply_relation = :supplierSupplier");
		}
		if (goodsName != null) {
			findSql.append(" and goods.name like :goodsName ");
		}
		if (productCategoryId != null) {
			findSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(findSql.toString(),Goods.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (supplierSupplier != null) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
			countQuery.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		if (goodsName != null) {
			query.setParameter("goodsName" , "%" + goodsName + "%");
			countQuery.setParameter("goodsName" , "%" + goodsName + "%");
		}
		if (productCategoryId != null) {
			query.setParameter("categoryId" , productCategoryId);
			countQuery.setParameter("categoryId" , productCategoryId);
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Goods>(query.getResultList(), total, pageable);
	}

	@Override
	public Page<Goods> findPageBySupplyNeed(SupplyNeed supplyNeed, String goodsName, Long productCategoryId,
			Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select DISTINCT goods.* from t_need_product needProduct ");
		findSql.append(" left join xx_product product on product.id=needProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		findSql.append(" where 1=1 and goods.deleted=0 ");
		if (supplyNeed != null) {
			findSql.append(" and needProduct.supply_need = :supplyNeed");
		}
		if (goodsName != null) {
			findSql.append(" and goods.name like :goodsName ");
		}
		if (productCategoryId != null) {
			findSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(findSql.toString(),Goods.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (supplyNeed != null) {
			query.setParameter("supplyNeed", supplyNeed.getId());
			countQuery.setParameter("supplyNeed", supplyNeed.getId());
		}
		if (goodsName != null) {
			query.setParameter("goodsName" , "%" + goodsName + "%");
			countQuery.setParameter("goodsName" , "%" + goodsName + "%");
		}
		if (productCategoryId != null) {
			query.setParameter("categoryId" , productCategoryId);
			countQuery.setParameter("categoryId" , productCategoryId);
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategoryId + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Goods>(query.getResultList(), total, pageable);
	}

	@Override
	public boolean existLocalDistributionGoods(Supplier supplier, Goods goods) {
		StringBuffer buffer = new StringBuffer("select DISTINCT goods.* from t_need_shop_product needShopProduct");
		buffer.append(" INNER JOIN xx_product product on needShopProduct.products = product.id");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1");
		buffer.append(" and needShopProduct.supplier = :supplier and goods.id = :goods");
		Query query = entityManager.createNativeQuery(buffer.toString(), Goods.class);
		query.setParameter("supplier", supplier.getId());
		query.setParameter("goods", goods.getId());
		List<Goods> list = query.getResultList();
		if(list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Goods> findbyProductCategoryList(ProductCategory productCategory) {
		try {
			StringBuffer buffer = new StringBuffer("select goods.* from xx_goods goods");
			buffer.append(" INNER JOIN xx_product_category productCategory on goods.product_category = productCategory.id where 1=1");
			if(productCategory != null) {
				buffer.append(" and productCategory.id = :productCategory");
			}
			buffer.append(" and goods.deleted = 0 and productCategory.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), Goods.class).setParameter("productCategory", productCategory.getId());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<Goods> findByMember(Member member, String goodsName) {
		try {
			StringBuffer findSql = new StringBuffer("select goods.* from xx_goods goods left join t_supplier supplier on goods.supplier = supplier.id") ;
			findSql.append(" where 1=1");
			if(null != member){
				findSql.append(" and supplier.member = :member") ;
			}
			if(StringUtils.isNotEmpty(goodsName)){
				findSql.append(" and goods.name = :goodsName") ;
			}
			findSql.append(" and goods.deleted = 0 and supplier.deleted = 0");
			Query query = entityManager.createNativeQuery(findSql.toString(), Goods.class);
			if(null != member){
				query.setParameter("member", member);
			}
			if(StringUtils.isNotEmpty(goodsName)){
				query.setParameter("goodsName", goodsName);
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	

}
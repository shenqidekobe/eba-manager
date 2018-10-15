/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.sf.ehcache.search.expression.And;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.ProductDao;
import com.microBusiness.manage.dto.ProductSupplierDto;
import com.microBusiness.manage.dto.SupplyProductDto;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Goods.OrderType;
import com.microBusiness.manage.entity.Goods.Type;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("productDaoImpl")
public class ProductDaoImpl extends BaseDaoImpl<Product, Long> implements ProductDao {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductDaoImpl.class);

	public boolean snExists(String sn, Supplier supplier) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String jpql = "select count(*) from Product product where lower(product.sn) = lower(:sn) and product.goods.supplier=:supplier and product.goods.deleted=:deleted";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).setParameter("supplier", supplier).setParameter("deleted", false).getSingleResult();
		return count > 0;
	}

	public Product findBySn(String sn, Supplier supplier) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}

		String jpql = "select product from Product product where lower(product.sn) = lower(:sn) and product.goods.supplier=:supplier";
		try {
			return entityManager.createQuery(jpql, Product.class).setParameter("sn", sn).setParameter("supplier", supplier).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Product> search(Goods.Type type, String keyword, Set<Product> excludes, Integer count) {
		if (StringUtils.isEmpty(keyword)) {
			return Collections.emptyList();
		}

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("goods").get("type"), type));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.get("goods").<String> get("name"), "%" + keyword + "%")));
		if (CollectionUtils.isNotEmpty(excludes)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(root.in(excludes)));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, null, null);
	}

	@Override
	public List<Product> getproductSpecifications(Long goodsId) {
		String sql = "SELECT np.products id,p.market_price marketPrice,p.price," +
				"p.reward_point rewardPoint,p.specification_values specificationValues, " +
				"FROM t_need_product np , xx_goods g ,xx_product p WHERE np.products = p.id " +
				"AND p.goods = :goodsId AND g.id = :goodsId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("goodsId", goodsId);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(Product.class));
		List<Product> resultList = query.getResultList();
		return resultList;
	}


	/**
	 * 获取商品正式供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Override
	public List<Product> getFormalProduct(Long goodsId, Long supplierId, Need need) {

		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != need){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != supplierId){
			findSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != goodsId){
			findSql.append(" and product.goods=:goodsId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(null != goodsId){
			query.setParameter("goodsId" , goodsId) ;
		}


		List<Product> result = query.getResultList() ;

		return result;


	}

	/**
	 * 获取商品临时供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Override
	public List<Product> getTemporaryProduct(Long goodsId, Long supplierId, Need need) {
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_need_product needPro on needPro.products=product.id");
		findSql.append(" inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true");
		findSql.append(" and supplyNeed.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(need!=null){
			findSql.append(" AND supplyNeed.need = :needId ");
		}
		if(supplierId!=null){
			findSql.append(" AND supplyNeed.supplier = :supplierId");
		}
		if(goodsId!=null){
			findSql.append(" AND product.goods = :goodsId ");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(goodsId!=null){
			query.setParameter("goodsId" , goodsId) ;
		}

		List<Product> result = query.getResultList() ;

		return result;
	}


	@Override
	public Page<Product> getTemporaryProductPage(Long supplierId, Long needId, Pageable pageable , Long productCategoryId,String searchName) {
		StringBuffer findSql = new StringBuffer("select product.*,assProduct.min_order_quantity from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");
		StringBuffer conditionSql = new StringBuffer();

		conditionSql.append(" inner join xx_goods goods on product.goods=goods.id");
		conditionSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		conditionSql.append(" inner join t_need_product assProduct on product.id=assProduct.products");
		conditionSql.append(" inner join t_supply_need supplierRel on supplierRel.id=assProduct.supply_need");
		conditionSql.append(" where 1=1 and goods.is_marketable=true and goods.deleted=0 ");

		if(null != needId){
			conditionSql.append(" and supplierRel.need=:needId");
		}
		if(null != supplierId){
			conditionSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != productCategoryId){
			conditionSql.append(" and goods.product_category=:productCategoryId");
		}
		conditionSql.append(" and supplierRel.deleted=0");
		if(StringUtils.isNotEmpty(searchName)){
			conditionSql.append(" and ( goods.sn like :searchName or goods.name like :searchName)");
		}
		conditionSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");


		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;

		if(null != needId){
			query.setParameter("needId" , needId);
			countQuery.setParameter("needId" , needId);
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
			countQuery.setParameter("supplierId" , supplierId) ;

		}

		if(null != productCategoryId){
			query.setParameter("productCategoryId" , productCategoryId) ;
			countQuery.setParameter("productCategoryId" , productCategoryId) ;
		}

		if(StringUtils.isNotEmpty(searchName)){
			query.setParameter("searchName", "%" + searchName + "%");
			countQuery.setParameter("searchName", "%" + searchName + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);
	}
	
	
	@Override
	public Page<Product> getFormalProductPage(Long supplierId, Long needId, Pageable pageable , Long productCategoryId,String searchName) {
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");
		StringBuffer conditionSql = new StringBuffer();

		conditionSql.append(" inner join xx_goods goods on product.goods=goods.id");
		conditionSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		conditionSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation");
		conditionSql.append(" where 1=1 and goods.deleted=0 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		conditionSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != needId){
			conditionSql.append(" and assProduct.need=:needId");
		}
		if(null != supplierId){
			conditionSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != productCategoryId){
			conditionSql.append(" and goods.product_category=:productCategoryId");
		}

		if(StringUtils.isNotEmpty(searchName) ){
			conditionSql.append(" and ( goods.sn like :searchName or goods.name like :searchName)");
		}


		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;

		if(null != needId){
			query.setParameter("needId" , needId);
			countQuery.setParameter("needId" , needId);
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
			countQuery.setParameter("supplierId" , supplierId) ;

		}

		if(null != productCategoryId){
			query.setParameter("productCategoryId" , productCategoryId) ;
			countQuery.setParameter("productCategoryId" , productCategoryId) ;
		}

		if(StringUtils.isNotEmpty(searchName) ){
			query.setParameter("searchName", "%" + searchName + "%");
			countQuery.setParameter("searchName", "%" + searchName + "%");
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);
	}

	@Override
	public Page<Product> getSupplierAssignProducts(Long supplierId, Pageable pageable , Long productCategoryId) {

		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");

		StringBuffer conditionSql = new StringBuffer();

		conditionSql.append(" inner join xx_goods goods on product.goods=goods.id");
		conditionSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		conditionSql.append(" where 1=1 and goods.deleted=0 and goods.is_marketable=true ");

		if(null != supplierId){
			conditionSql.append(" and goods.supplier=:supplierId");
		}

		if(null != productCategoryId){
			conditionSql.append(" and goods.product_category=:productCategoryId");
		}
		conditionSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("sn")){
				conditionSql.append(" and goods.sn like :searchValue");
			}
			if(searchProperty.equals("name")){
				conditionSql.append(" and goods.name like :searchValue");
			}
		}

		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;

		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
			countQuery.setParameter("supplierId" , supplierId) ;

		}

		if(null != productCategoryId){
			query.setParameter("productCategoryId" , productCategoryId) ;
			countQuery.setParameter("productCategoryId" , productCategoryId) ;
		}

		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){

			if(searchProperty.equals("sn")){
				query.setParameter("searchValue", Long.valueOf(searchValue));
				countQuery.setParameter("searchValue", Long.valueOf(searchValue));
			}
			if(searchProperty.equals("name")){
				query.setParameter("searchValue", "%" + searchValue + "%");
				countQuery.setParameter("searchValue", "%" + searchValue + "%");
			}

		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);

	}
	
	@Override
	public Page<Product> getSupplierAssignProductsFormal(Long supplierId, Long bySupplierId, Pageable pageable , Long productCategoryId) {

		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");

		StringBuffer conditionSql = new StringBuffer();

		conditionSql.append(" inner join xx_goods goods on product.goods=goods.id");
		conditionSql.append(" inner join t_supplier_product supplyProduct on product.id=supplyProduct.products");
		conditionSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=supplyProduct.supply_relation");
		conditionSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		conditionSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());


		if(null != supplierId){
			conditionSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != bySupplierId){
			conditionSql.append(" and supplierRel.by_supplier=:bySupplierId");
		}

		if(null != productCategoryId){
			conditionSql.append(" and goods.product_category=:productCategoryId");
		}

		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){
			if(searchProperty.equals("sn")){
				conditionSql.append(" and goods.sn like :searchValue");
			}
			if(searchProperty.equals("name")){
				conditionSql.append(" and goods.name like :searchValue");
			}
		}

		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;

		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
			countQuery.setParameter("supplierId" , supplierId) ;

		}

		if(null != bySupplierId){
			query.setParameter("bySupplierId" , bySupplierId) ;
			countQuery.setParameter("bySupplierId" , bySupplierId) ;

		}

		if(null != productCategoryId){
			query.setParameter("productCategoryId" , productCategoryId) ;
			countQuery.setParameter("productCategoryId" , productCategoryId) ;
		}

		if(StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)){

			if(searchProperty.equals("sn")){
				query.setParameter("searchValue", Long.valueOf(searchValue));
				countQuery.setParameter("searchValue", Long.valueOf(searchValue));
			}
			if(searchProperty.equals("name")){
				query.setParameter("searchValue", "%" + searchValue + "%");
				countQuery.setParameter("searchValue", "%" + searchValue + "%");
			}

		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);

	}

	/**
	 * 获取商品正式供应的规格
	 * 方法有问题，效率不高 , 应该反向查询，先确定关系，再确定商品，效率应该会高？？
	 * @param goodsId
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Override
	public List<SupplyProductDto> getFormalSupplyProduct(Long goodsId, Long supplierId, Need need) {

		StringBuffer findSql = new StringBuffer("select product.* , supplierPro.min_order_quantity as minOrderQuantity,supplierPro.supply_price as supplyPrice from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_product supplierPro on assProduct.products=supplierPro.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation and supplierPro.supply_relation=supplierRel.id");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != need){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != supplierId){
			findSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != goodsId){
			findSql.append(" and product.goods=:goodsId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(null != goodsId){
			query.setParameter("goodsId" , goodsId) ;
		}

		query.unwrap(SQLQuery.class).addEntity(Product.class).addScalar("minOrderQuantity" , IntegerType.INSTANCE).addScalar("supplyPrice" , BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(SupplyProductDto.class));

		List<SupplyProductDto> result = query.getResultList() ;

		return result;
	}

	/**
	 * 获取商品临时供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Override
	public List<SupplyProductDto> getTemporarySupplyProduct(Long goodsId, Long supplierId, Need need) {
		StringBuffer findSql = new StringBuffer("select product.* , needPro.min_order_quantity as minOrderQuantity,needPro.supply_price as supplyPrice from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_need_product needPro on needPro.products=product.id");
		findSql.append(" inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true");
		findSql.append(" and supplyNeed.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(need!=null){
			findSql.append(" AND supplyNeed.need = :needId ");
		}
		if(supplierId!=null){
			findSql.append(" AND supplyNeed.supplier = :supplierId");
		}
		if(goodsId!=null){
			findSql.append(" AND product.goods = :goodsId ");
		}

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(goodsId!=null){
			query.setParameter("goodsId" , goodsId) ;
		}

		query.unwrap(SQLQuery.class).addEntity(Product.class).addScalar("minOrderQuantity" , IntegerType.INSTANCE).addScalar("supplyPrice" , BigDecimalType.INSTANCE).setResultTransformer(Transformers.aliasToBean(SupplyProductDto.class));

		List<SupplyProductDto> result = query.getResultList() ;

		return result;
	}


	/**
	 * @param product
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Override
	public Integer getTemporarySupplyProduct(Product product, Long supplierId, Need need) {

		StringBuffer findSql = new StringBuffer("select needPro.min_order_quantity as minOrderQuantity from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_need_product needPro on needPro.products=product.id");
		findSql.append(" inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true");
		findSql.append(" and supplyNeed.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(need!=null){
			findSql.append(" AND supplyNeed.need = :needId ");
		}
		if(supplierId!=null){
			findSql.append(" AND supplyNeed.supplier = :supplierId");
		}
		if(null != product){
			findSql.append(" AND product.id = :productId ");
		}

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(null != product){
			query.setParameter("productId" , product.getId()) ;
		}

		try{
			return (Integer) query.getSingleResult() ;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getTemporarySupplyProduct error :" , e);
			return 1 ;
		}

	}

	/**
	 * 方法有问题，效率不高 , 应该反向查询，先确定关系，再确定商品，效率应该会高？？
	 * @param product
	 * @param supplierId
	 * @param need
	 * @return
	 */
	@Deprecated
	@Override
	public Integer getFormalSupplyProduct(Product product, Long supplierId, Need need) {
		StringBuffer findSql = new StringBuffer("select supplierPro.min_order_quantity as minOrderQuantity from xx_product product");
		findSql.append(" inner join xx_goods goods on product.goods=goods.id");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_product supplierPro on assProduct.products=supplierPro.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation and supplierPro.supply_relation=supplierRel.id");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != need){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != supplierId){
			findSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != product){
			findSql.append(" and product.id=:productId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		if(null != product){
			query.setParameter("productId" , product.getId()) ;
		}

		try{
			return (Integer) query.getSingleResult() ;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getFormalSupplyProduct error :" , e);
			return 1 ;
		}

	}


	/**
	 * 获取商品正式供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param needId
	 * @return
	 */
	@Override
	public List<Product> getFormalProduct(Long goodsId, Long supplierId, Long needId) {
		return null;
	}

	/**
	 * 获取商品临时供应的规格
	 *
	 * @param goodsId
	 * @param supplierId
	 * @param needId
	 * @return
	 */
	@Override
	public List<Product> getTemporaryProduct(Long goodsId, Long supplierId, Long needId) {
		return null;
	}
	
	@Override

	public Page<Product> getGoodListPage(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,
			ProductCategory productCategory, String goodName,Pageable pageable,Long[] ids) {
		
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(" left join xx_goods goods on goods.id=product.goods ");
		if (supplyNeed != null) {
			conditionSql.append(" inner join t_need_product needProduct on needProduct.products=product.id ");
			conditionSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		}
		conditionSql.append(" left join xx_goods goodSource on goodSource.id=goods.source ");
		conditionSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		conditionSql.append(" where 1=1  and goods.deleted=0 ");
		
		if (supplyNeed != null) {
			conditionSql.append(" and needProduct.supply_need=:supplyNeed ");
			conditionSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
		}
		if (cruuSupplier != null) {
			conditionSql.append(" and goods.supplier=:cruuSupplier ");
		}
		if (goodName != null) {
			conditionSql.append(" and goods.name like :goodName ");
		}
		if (productCategory != null) {
			conditionSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		if (supplier !=null) {
			conditionSql.append("and goods.source is not null and goodSource.supplier=:supplierId ");
		}else {
			if (hasDistribution) {
				conditionSql.append("and goods.source is null ");
			}
		}
		if (ids != null && ids.length>0){
			conditionSql.append(" and product.id not in(:ids) ");
		}
		
		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;
		
		if (supplyNeed != null) {
			query.setParameter("supplyNeed" , supplyNeed.getId());
			countQuery.setParameter("supplyNeed" , supplyNeed.getId());
		}
		if (cruuSupplier != null) {
			query.setParameter("cruuSupplier" , cruuSupplier.getId());
			countQuery.setParameter("cruuSupplier" , cruuSupplier.getId());
		}
		if (goodName != null) {
			query.setParameter("goodName" , "%" + goodName + "%");
			countQuery.setParameter("goodName" , "%" + goodName + "%");
		}
		if (productCategory != null) {

			query.setParameter("categoryId" , productCategory.getId());
			countQuery.setParameter("categoryId" , productCategory.getId());
			
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}
		if (supplier !=null) {
			query.setParameter("supplierId" , supplier.getId());
			countQuery.setParameter("supplierId" , supplier.getId());
		}
		if (ids != null && ids.length>0){
			query.setParameter("ids" , Arrays.asList(ids));
			countQuery.setParameter("ids" , Arrays.asList(ids));
		}
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);
		
	}
	
	@Override
	public List<Product> getGoodList(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution,
			ProductCategory productCategory, String goodName,Pageable pageable) {
		
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer conditionSql = new StringBuffer();
		if (supplyNeed != null) {
			conditionSql.append(" inner join t_need_product needProduct on needProduct.products=product.id ");
		}
		conditionSql.append(" left join xx_goods goods on goods.id=product.goods ");
		conditionSql.append(" left join xx_goods goodSource on goodSource.id=goods.source ");
		conditionSql.append(" where 1=1 and goods.deleted=0 ");
		
		if (supplyNeed != null) {
			conditionSql.append(" and needProduct.supply_need=:supplyNeed ");
		}
		if (cruuSupplier != null) {
			conditionSql.append(" and goods.supplier=:cruuSupplier ");
		}
		if (supplier !=null) {
			conditionSql.append("and goods.source is not null and goodSource.supplier=:supplierId ");
		}else {
			if (hasDistribution) {
				conditionSql.append("and goods.source is null ");
			}
		}
		
		findSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		
		if (supplyNeed != null) {
			query.setParameter("supplyNeed" , supplyNeed.getId());
		}
		if (cruuSupplier != null) {
			query.setParameter("cruuSupplier" , cruuSupplier.getId());
		}
		if (supplier !=null) {
			query.setParameter("supplierId" , supplier.getId());
		}
		
		List<Product> result = query.getResultList() ;
		return result;
	}
	
	@Override
	public Page<Product> getGoodListPageByFormal(SupplierSupplier supplierSupplier,Need need,
			ProductCategory productCategory, String goodName,Pageable pageable,Long[] ids) {
		
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer countSql = new StringBuffer("select count(*) from xx_product product");
		StringBuffer conditionSql = new StringBuffer();
		if (need != null) {
			conditionSql.append(" inner join t_supplier_need_product needProduct on needProduct.products=product.id ");
		}else {
			conditionSql.append(" inner join t_supplier_product needProduct on needProduct.products=product.id ");
		}
		conditionSql.append(" left join xx_goods goods on goods.id=product.goods ");
		conditionSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		conditionSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		conditionSql.append(" where 1=1 and goods.deleted=0 ");
		
		if (supplierSupplier != null) {
			conditionSql.append(" and needProduct.supply_relation=:supplierSupplier ");
		}
		if (need != null) {
			conditionSql.append(" and needProduct.need=:need ");
		}
		if (goodName != null) {
			conditionSql.append(" and goods.name like :goodName ");
		}
		if (productCategory != null) {
			conditionSql.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		if (ids != null && ids.length>0){
			conditionSql.append(" and product.id not in(:ids) ");
		}
		conditionSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
		
		findSql.append(conditionSql);
		countSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		Query countQuery = entityManager.createNativeQuery(countSql.toString()) ;
		
		if (supplierSupplier != null) {
			query.setParameter("supplierSupplier" , supplierSupplier.getId());
			countQuery.setParameter("supplierSupplier" , supplierSupplier.getId());
		}
		if (need != null) {
			query.setParameter("need" , need.getId());
			countQuery.setParameter("need" , need.getId());
		}
		if (goodName != null) {
			query.setParameter("goodName" , "%" + goodName + "%");
			countQuery.setParameter("goodName" , "%" + goodName + "%");
		}
		if (productCategory != null) {
			query.setParameter("categoryId" , productCategory.getId());
			countQuery.setParameter("categoryId" , productCategory.getId());			
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}
		if (ids != null && ids.length>0){
			query.setParameter("ids" , Arrays.asList(ids));
			countQuery.setParameter("ids" , Arrays.asList(ids));
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}

		List<Product> result = query.getResultList() ;

		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new Page<Product>(query.getResultList(), total, pageable);
		
	}
	
	@Override
	public List<Product> getGoodListByFormal(SupplierSupplier supplierSupplier,Need need) {
		
		StringBuffer findSql = new StringBuffer("select product.* from xx_product product");
		StringBuffer conditionSql = new StringBuffer();
		if (need != null) {
			conditionSql.append(" inner join t_supplier_need_product needProduct on needProduct.products=product.id ");
		}else {
			conditionSql.append(" inner join t_supplier_product needProduct on needProduct.products=product.id ");
		}
		conditionSql.append(" left join xx_goods goods on goods.id=product.goods ");
		conditionSql.append(" where 1=1  ");
		
		if (supplierSupplier != null) {
			conditionSql.append(" and needProduct.supply_relation=:supplierSupplier ");
		}
		if (need != null) {
			conditionSql.append(" and needProduct.need=:need ");
		}
		
		findSql.append(conditionSql);

		Query query = entityManager.createNativeQuery(findSql.toString() , Product.class) ;
		
		if (supplierSupplier != null) {
			query.setParameter("supplierSupplier" , supplierSupplier.getId());
		}
		if (need != null) {
			query.setParameter("need" , need.getId());
		}
		
		List<Product> result = query.getResultList() ;
		return result;

	}

	@Override
	public Page<Product> findPage(Type type, ProductCategory productCategory,
			Brand brand, Promotion promotion, Tag tag,
			Map<Attribute, String> attributeValueMap, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isOutOfStock, Boolean isStockAlert,
			Boolean hasPromotion, OrderType orderType, Pageable pageable,
			Supplier supplier, Boolean sourceFlag, Boolean branchFlag,String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		Join<Product, Goods> goodsJoin = root.join("goods" , JoinType.LEFT);
		Join<Goods , SupplierSupplier> goodSupplierJoin = goodsJoin.join("supplierSupplier" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("deleted"), false));
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("type"), type));
		}
		if (productCategory != null) {
			Subquery<ProductCategory> subquery = criteriaQuery.subquery(ProductCategory.class);
			Root<ProductCategory> subqueryRoot = subquery.from(ProductCategory.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(goodsJoin.get("productCategory")).value(subquery));
		}
		
		if (null != sourceFlag) {
			if (sourceFlag) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(goodsJoin.get("source")));
			}
		}
		
		if (null != branchFlag) {
			if (branchFlag) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(goodSupplierJoin.isNull(),criteriaBuilder.and(criteriaBuilder.equal(goodSupplierJoin.get("status"), SupplierSupplier.Status.inTheSupply),criteriaBuilder.lessThanOrEqualTo(goodSupplierJoin.<Date> get("startDate"), new Date()),criteriaBuilder.greaterThanOrEqualTo(goodSupplierJoin.<Date>get("endDate"), new Date()))));
			}
		}
		
		if (null != supplier) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("supplier"), supplier));
		}

		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("brand"), brand));
		}
		if (promotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.join("promotions"), promotion));
		}
		if(null != searchName) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(goodsJoin.<String>get("name") , "%"+searchName+"%")));
		}
		if (tag != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.join("tags"), tag));
		}
		if (attributeValueMap != null) {
			for (Map.Entry<Attribute, String> entry : attributeValueMap.entrySet()) {
				String propertyName = Goods.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(goodsJoin.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(BigDecimal.ZERO) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(goodsJoin.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("isTop"), isTop));
		}
		if (isOutOfStock != null) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			Path<Integer> stock = subqueryRoot.get("stock");
			Path<Integer> allocatedStock = subqueryRoot.get("allocatedStock");
			if (isOutOfStock) {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), goodsJoin), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), goodsJoin), criteriaBuilder.greaterThan(stock, allocatedStock));
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
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), goodsJoin), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			} else {
				subquery.where(criteriaBuilder.equal(subqueryRoot.get("goods"), goodsJoin), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (hasPromotion != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(goodsJoin.join("promotions")));
		}
		criteriaQuery.where(restrictions);
		if (orderType != null) {
			switch (orderType) {
				case topDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("isTop")), criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
				case priceAsc:
					criteriaQuery.orderBy(criteriaBuilder.asc(goodsJoin.get("price")), criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
				case priceDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("price")), criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
				case salesDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("sales")), criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
				case scoreDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("score")), criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
				case dateDesc:
					criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("createDate")));
					break;
			}
		} else if (pageable == null || ((StringUtils.isEmpty(pageable.getOrderProperty()) || pageable.getOrderDirection() == null) && (CollectionUtils.isEmpty(pageable.getOrders())))) {
			criteriaQuery.orderBy(criteriaBuilder.desc(goodsJoin.get("isTop")), criteriaBuilder.desc(goodsJoin.get("createDate")));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<NeedProduct> findPage(SupplyNeed supplyNeed, Pageable pageable,
			Long productCategoryId) {
		try {
			String searchValue=pageable.getSearchValue();
			StringBuffer buffer = new StringBuffer("select needProduct.* from t_supply_need supplyNeed");
			buffer.append(" INNER JOIN  t_need_product needProduct on supplyNeed.id = needProduct.supply_need");
			buffer.append(" INNER JOIN xx_product product on needProduct.products = product.id");
			buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id ");
			buffer.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
			buffer.append("where 1=1 and goods.deleted=0 ");
			if(null != supplyNeed) {
				buffer.append(" and supplyNeed.id = :id");
			}
			if(null != searchValue) {
				buffer.append(" and goods.name like :searchValue");
			}
			if(null != productCategoryId) {
				buffer.append(" and goods.product_category = :productCategoryId");
			}
			buffer.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
			
			StringBuffer countSql = new StringBuffer("select count(needProduct.id) from t_supply_need supplyNeed");
			countSql.append(" INNER JOIN  t_need_product needProduct on supplyNeed.id = needProduct.supply_need");
			countSql.append(" INNER JOIN xx_product product on needProduct.products = product.id");
			countSql.append(" INNER JOIN xx_goods goods on product.goods = goods.id ");
			countSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
			countSql.append("where 1=1 and goods.deleted=0 ");
			if(null != supplyNeed) {
				countSql.append(" and supplyNeed.id = :id");
			}
			if(null != searchValue) {
				countSql.append(" and goods.name like :searchValue");
			}
			if(null != productCategoryId) {
				countSql.append(" and goods.product_category = :productCategoryId");
			}
			countSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
			
			Query query = entityManager.createNativeQuery(buffer.toString(), NeedProduct.class);
			Query count = entityManager.createNativeQuery(countSql.toString());
			
			if(null != supplyNeed) {
				query.setParameter("id", supplyNeed.getId());
				count.setParameter("id", supplyNeed.getId());
			}
			if(null != searchValue) {
				query.setParameter("searchValue", "%"+searchValue+"%");
				count.setParameter("searchValue", "%"+searchValue+"%");
			}
			if(null != productCategoryId) {
				query.setParameter("productCategoryId", productCategoryId);
				count.setParameter("productCategoryId", productCategoryId);
			}
			
			BigInteger tempTotal = (BigInteger) count.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			List<NeedProduct> list = query.getResultList();
			return new Page<NeedProduct>(list, total, pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 
	 * @Title: findList
	 * @author: yuezhiwei
	 * @date: 2018年2月5日下午4:46:02
	 * @Description: 商品列表同步分销商品会用到，查询企业供应中的product
	 * @return: List<Product>
	 */
	@Override
	public List<Product> findList(SupplierSupplier supplierSupplier,
			List<Long> productIds) {
		StringBuffer buffer = new StringBuffer("select product.* from t_supplier_product supplierProduct");
		buffer.append(" INNER JOIN xx_product product on supplierProduct.products = product.id where 1=1");
		buffer.append(" and supplierProduct.supply_relation = :supplierSupplier");
		buffer.append(" and supplierProduct.products in(:productIds)");
		Query query = entityManager.createNativeQuery(buffer.toString(), Product.class);
		query.setParameter("supplierSupplier", supplierSupplier);
		query.setParameter("productIds", productIds);
		return query.getResultList();
	}


	@Override
	public List<Product> supplyDistributionProducts(Need need,
			SupplierSupplier supplierSupplier) {
		StringBuffer buffer = new StringBuffer("select product.* from t_supplier_need_product supplierNeedProduct");
		buffer.append(" INNER JOIN xx_product product on supplierNeedProduct.products = product.id where 1=1");
		if(null != need) {
			buffer.append(" and supplierNeedProduct.need = :need");
		}
		if(null != supplierSupplier) {
			buffer.append(" and supplierNeedProduct.supply_relation = :supplierSupplier");
		}
		buffer.append(" and supplierNeedProduct.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString(), Product.class);
		if(null != need) {
			query.setParameter("need", need.getId());
		}
		if(null != supplierSupplier) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		return query.getResultList();
	}

	@Override
	public List<Product> supplyProducts(SupplierSupplier supplierSupplier) {
		StringBuffer buffer = new StringBuffer("select product.* from t_supplier_product supplierProduct");
		buffer.append(" INNER JOIN xx_product product on supplierProduct.products = product.id where 1=1");
		if(null != supplierSupplier) {
			buffer.append(" and supplierProduct.supply_relation = :supplierSupplier");
		}
		buffer.append(" and supplierProduct.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString(), Product.class);
		if(null != supplierSupplier) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		return query.getResultList();
	}

	@Override
	public Page<Product> findPageSmall(Supplier supplier, ProductCategory productCategory, String goodsName, Long[] ids, Pageable pageable) {
		StringBuffer buffer = new StringBuffer(" select product.* from xx_product product ");
		buffer.append(" left join xx_goods goods on product.goods=goods.id ");
		buffer.append(" left join xx_product_category category on category.id=goods.product_category ");
		buffer.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		buffer.append(" where 1=1 and goods.deleted=0 ");
		if (supplier != null){
			buffer.append(" and goods.supplier=:supplier ");
		}
		if (goodsName != null){
			buffer.append(" and goods.name  like :goodsName ");
		}
		if (productCategory != null) {
			buffer.append(" and (category.id=:categoryId or category.tree_path like :treePath) ");
		}
		if (ids != null && ids.length>0){
			buffer.append(" and product.id not in(:ids) ");
		}
		buffer.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(buffer);
		countSql.append(" ) datas ");

		Query query = entityManager.createNativeQuery(buffer.toString(),Product.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());

		if (supplier != null){
			query.setParameter("supplier" , supplier.getId());
			countQuery.setParameter("supplier" ,supplier.getId());
		}

		if (goodsName != null) {
			query.setParameter("goodsName" , "%" + goodsName + "%");
			countQuery.setParameter("goodsName" , "%" + goodsName + "%");
		}
		if (productCategory != null) {
			query.setParameter("categoryId" , productCategory.getId());
			countQuery.setParameter("categoryId" , productCategory.getId());
			query.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
			countQuery.setParameter("treePath" , "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}
		if (ids != null && ids.length>0){
			query.setParameter("ids" , Arrays.asList(ids));
			countQuery.setParameter("ids" , Arrays.asList(ids));
		}

		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();

		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		List<Product> list = query.getResultList();

		return new Page<Product>(list, total, pageable);
	}



	@Override
	public List<Product> findList(String goodsSn, Supplier supplier) {
		StringBuffer buffer = new StringBuffer("select product.* from xx_goods goods INNER JOIN");
		buffer.append(" xx_product product on goods.id = product.goods where 1=1");
		buffer.append(" and goods.sn = :goodsSn and goods.supplier = :supplier");
		buffer.append(" and goods.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString(), Product.class);
		query.setParameter("goodsSn", goodsSn);
		query.setParameter("supplier", supplier.getId());
		return query.getResultList();
	}

	@Override
	public List<ProductSupplierDto> findByProductAndSupplier(Member member,
			String barCode, String goodsName, Types types, Long shopId) {
		//托管门店
		StringBuffer buffer = new StringBuffer("select product.*,supplier.*,goods.name goodsName from xx_product product");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id");
		buffer.append(" INNER JOIN t_supplier supplier on goods.supplier = supplier.id");
		buffer.append(" INNER JOIN xx_member member on supplier.member = member.id");
		buffer.append(" INNER JOIN t_hosting_shop hostingShop on hostingShop.member = member.id where 1=1");
		if(shopId != null){
			buffer.append(" and hostingShop.shop = :shop");
		}
		if(null != member) {
			buffer.append(" and hostingShop.by_member = :member");
		}
		if(StringUtils.isNotEmpty(barCode)) {
			buffer.append(" and product.bar_code = :barCode");
		}
		if(null != types) {
			buffer.append(" and supplier.types = :types");
		}
		if(StringUtils.isNotEmpty(goodsName)) {
			buffer.append(" and goods.name like :goodsName");
		}
		buffer.append(" and supplier.deleted = 0");
		buffer.append(" and goods.deleted = 0");
		
		buffer.append(" UNION ALL");
		//自己的门店
		buffer.append(" select product.*,supplier.*,goods.name goodsName from xx_product product");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id");
		buffer.append(" INNER JOIN t_supplier supplier on goods.supplier = supplier.id where 1=1");
		if(null != member) {
			buffer.append(" and supplier.member = :member");
		}
		if(StringUtils.isNotEmpty(barCode)) {
			buffer.append(" and product.bar_code = :barCode");
		}
		if(null != types) {
			buffer.append(" and supplier.types = :types");
		}
		if(StringUtils.isNotEmpty(goodsName)) {
			buffer.append(" and goods.name like :goodsName");
		}
		buffer.append(" and supplier.deleted = 0");
		buffer.append(" and goods.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(shopId != null){
			query.setParameter("shop", shopId);
		}
		if(null != member) {
			query.setParameter("member", member.getId());
		}
		if(StringUtils.isNotEmpty(barCode)) {
			query.setParameter("barCode", barCode);
		}
		if(null != types) {
			query.setParameter("types", types.ordinal());
		}
		if(StringUtils.isNotEmpty(goodsName)) {
			query.setParameter("goodsName", "%"+goodsName+"%");
		}
		
		query.unwrap(SQLQuery.class)
			.addEntity(Product.class)
			.addEntity(Supplier.class)
			.addScalar("goodsName" , StringType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(ProductSupplierDto.class));
		List<ProductSupplierDto> list = query.getResultList();
		return list;
	}

	@Override
	public List<ProductSupplierDto> findByProductAndSupplier(Member member,
			String searchName, Types types) {
		StringBuffer buffer = new StringBuffer("select product.*,supplier.*,goods.name goodsName from xx_product product");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id");
		buffer.append(" INNER JOIN t_supplier supplier on goods.supplier = supplier.id");
		buffer.append(" INNER JOIN xx_member member on supplier.member = member.id");
		buffer.append(" INNER JOIN t_hosting_shop hostingShop on hostingShop.member = member.id where 1=1");
		if(null != member) {
			buffer.append(" and hostingShop.by_member = :member");
		}
		if(StringUtils.isNotEmpty(searchName)) {
			buffer.append(" and (goods.name like :searchName or product.bar_code like :searchName)");
		}
		if(null != types) {
			buffer.append(" and supplier.types = :types");
		}
		buffer.append(" and supplier.deleted = 0");
		buffer.append(" and goods.deleted = 0");
		
		buffer.append(" UNION ALL");
		
		buffer.append(" select product.*,supplier.*,goods.name goodsName from xx_product product");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id");
		buffer.append(" INNER JOIN t_supplier supplier on goods.supplier = supplier.id where 1=1");
		if(null != member) {
			buffer.append(" and supplier.member = :member");
		}
		if(StringUtils.isNotEmpty(searchName)) {
			buffer.append(" and (goods.name like :searchName or product.bar_code like :searchName)");
		}
		if(null != types) {
			buffer.append(" and supplier.types = :types");
		}
		buffer.append(" and supplier.deleted = 0");
		buffer.append(" and goods.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != member) {
			query.setParameter("member", member.getId());
		}
		if(StringUtils.isNotEmpty(searchName)) {
			query.setParameter("searchName", "%"+searchName+"%");
		}
		if(null != types) {
			query.setParameter("types", types.ordinal());
		}
		
		query.unwrap(SQLQuery.class).addEntity(Product.class).addEntity(Supplier.class).addScalar("goodsName" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(ProductSupplierDto.class));
		List<ProductSupplierDto> list = query.getResultList();
		return list;
	}

	@Override
	public List<ProductSupplierDto> findShopByProductAndSupplier(Member member,
			String barCode, Types types, Shop shop) {
		StringBuffer buffer = new StringBuffer("select product.*,supplier.*,goods.name goodsName from xx_product product");
		buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id");
		buffer.append(" INNER JOIN t_supplier supplier on goods.supplier = supplier.id");
		buffer.append(" INNER JOIN xx_member member on supplier.member = member.id");
		buffer.append(" INNER JOIN t_shop shop on shop.member = member.id where 1=1");
		if(shop != null) {
			buffer.append(" and shop.id = :shop");
		}
		if(StringUtils.isNotEmpty(barCode)) {
			buffer.append(" and product.bar_code = :barCode");
		}
		if(types != null) {
			buffer.append(" and supplier.types = :types");
		}
		
		buffer.append(" and supplier.deleted = 0");
		buffer.append(" and goods.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(shop != null) {
			query.setParameter("shop", shop.getId());
		}
		if(StringUtils.isNotEmpty(barCode)) {
			query.setParameter("barCode", barCode);
		}
		if(null != types) {
			query.setParameter("types", types.ordinal());
		}
		query.unwrap(SQLQuery.class).addEntity(Product.class).addEntity(Supplier.class).addScalar("goodsName" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(ProductSupplierDto.class));
		List<ProductSupplierDto> list = query.getResultList();
		return list;
	}

}
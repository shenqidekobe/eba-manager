/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Order;
import com.microBusiness.manage.dao.ProductCategoryDao;
import com.microBusiness.manage.entity.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import com.sun.xml.bind.v2.model.core.ID;

@Repository("productCategoryDaoImpl")
public class ProductCategoryDaoImpl extends BaseDaoImpl<ProductCategory, Long> implements ProductCategoryDao {

	public List<ProductCategory> findRoots(Integer count) {
		String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent is null order by productCategory.order asc";
		TypedQuery<ProductCategory> query = entityManager.createQuery(jpql, ProductCategory.class);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count) {
		if (productCategory == null || productCategory.getParent() == null) {
			return Collections.emptyList();
		}
		TypedQuery<ProductCategory> query;
		if (recursive) {
			String jpql = "select productCategory from ProductCategory productCategory where productCategory.id in (:ids) order by productCategory.grade asc";
			query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("ids", Arrays.asList(productCategory.getParentIds()));
		} else {
			String jpql = "select productCategory from ProductCategory productCategory where productCategory = :productCategory";
			query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("productCategory", productCategory.getParent());
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	/**
	 *
	 * @param productCategory
	 * @param recursive 是否递归
	 * @param count
	 * @return
	 */
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		TypedQuery<ProductCategory> query;
		if (recursive) {
			if (productCategory != null) {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.treePath like :treePath order by productCategory.grade asc, productCategory.order asc";
				query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
			} else {
				String jpql = "select productCategory from ProductCategory productCategory order by productCategory.grade asc, productCategory.order asc";
				query = entityManager.createQuery(jpql, ProductCategory.class);
			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProductCategory> result = query.getResultList();
			sort(result);
			return result;
		} else {
			String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent = :parent order by productCategory.order asc";
			query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("parent", productCategory);
			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}

	private void sort(List<ProductCategory> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
		for (ProductCategory productCategory : productCategories) {
			orderMap.put(productCategory.getId(), productCategory.getOrder());
		}
		Collections.sort(productCategories, new Comparator<ProductCategory>() {
			@Override
			public int compare(ProductCategory productCategory1, ProductCategory productCategory2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(productCategory1.getParentIds(), productCategory1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(productCategory2.getParentIds(), productCategory2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(productCategory1.getGrade(), productCategory2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}


	@Override
	public List<ProductCategory> findRoots(Integer count, Supplier supplier) {
		String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent is null and productCategory.supplier=:supplier order by productCategory.order asc";
		TypedQuery<ProductCategory> query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("supplier", supplier);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public List<ProductCategory> findParents(ProductCategory productCategory, boolean recursive, Integer count, Supplier supplier) {
		return null;
	}

	@Override
	public List<ProductCategory> findChildren(ProductCategory productCategory, boolean recursive, Integer count, Supplier supplier , String searchName) {
		TypedQuery<ProductCategory> query;
		StringBuffer queryStr = new StringBuffer() ;
		if (recursive) {
			if (productCategory != null) {
				queryStr.append("select productCategory from ProductCategory productCategory where productCategory.deleted=0 and productCategory.treePath like :treePath") ;
				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and productCategory.name like :searchName");
				}
				//queryStr.append(" and productCategory.types=0");

				queryStr.append(" order by productCategory.grade asc, productCategory.order asc");

				//String jpql = "select productCategory from ProductCategory productCategory where productCategory.treePath like :treePath order by productCategory.grade asc, productCategory.order asc";

				query = entityManager.createQuery(queryStr.toString(), ProductCategory.class).setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			} else {
				//String jpql = "select productCategory from ProductCategory productCategory order by productCategory.grade asc, productCategory.order asc";

				queryStr.append("select productCategory from ProductCategory productCategory where 1=1 and productCategory.deleted=0") ;

				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and productCategory.name like :searchName");
				}
				//queryStr.append(" and productCategory.source is null");
				//queryStr.append(" and productCategory.types=0");

				queryStr.append(" order by productCategory.grade asc, productCategory.order asc");

				query = entityManager.createQuery(queryStr.toString() , ProductCategory.class);

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProductCategory> result = query.getResultList();
			sort(result);
			return result;
		} else {
			//String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent = :parent order by productCategory.order asc";

			queryStr.append("select productCategory from ProductCategory productCategory where 1=1 and  productCategory.deleted=0 and productCategory.parent = :parent") ;

			if(null != supplier){
				queryStr.append(" and supplier=:supplier");
			}
			if(null != searchName) {
				queryStr.append(" and productCategory.name like :searchName");
			}
			//queryStr.append(" and productCategory.types=0");

			queryStr.append(" order by productCategory.grade asc, productCategory.order asc");


			query = entityManager.createQuery(queryStr.toString(), ProductCategory.class).setParameter("parent", productCategory);

			if(null != supplier){
				query.setParameter("supplier" , supplier);
			}
			if(null != searchName) {
				query.setParameter("searchName" , "%" + searchName + "%");
			}

			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}


	@Override
	public List<ProductCategory> findByTemporary(Need need, Long relationId) {
		/*select category.* from xx_product_category category
		inner join xx_goods goods on goods.product_category=category.id
		inner join xx_product product on goods.id=product.goods
		inner join t_need_product needPro on needPro.products=product.id
		inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need*/

		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_need_product needPro on needPro.products=product.id");
		findSql.append(" inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true");
		findSql.append(" and supplyNeed.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(need!=null){
			findSql.append(" AND supplyNeed.need = :needId ");
		}
		if(relationId!=null){
			findSql.append(" AND supplyNeed.id = :relationId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != relationId){
			query.setParameter("relationId" , relationId) ;
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<ProductCategory> findByFormal(Need need, Long relationId) {

		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation");
		
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != need){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != relationId){
			findSql.append(" and supplierRel.id=:relationId");
		}
		
		

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != need){
			query.setParameter("needId" , need.getId());
		}
		if(null != relationId){
			query.setParameter("relationId" , relationId) ;
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}


	@Override
	public List<ProductCategory> findByTemporary(Long needId, Long supplierId) {
		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_need_product needPro on needPro.products=product.id");
		findSql.append(" inner join t_supply_need supplyNeed on supplyNeed.id = needPro.supply_need");
		findSql.append(" where 1=1 and goods.is_marketable=true");
		findSql.append(" and supplyNeed.status=").append(SupplyNeed.Status.SUPPLY.ordinal());

		if(needId!=null){
			findSql.append(" AND supplyNeed.need = :needId ");
		}
		if(supplierId!=null){
			findSql.append(" AND supplyNeed.supplier = :supplierId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != needId){
			query.setParameter("needId" , needId);
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<ProductCategory> findByFormal(Long needId, Long supplierId) {
		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_supplier_need_product assProduct on product.id=assProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assProduct.supply_relation");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != needId){
			findSql.append(" and assProduct.need=:needId");
		}
		if(null != supplierId){
			findSql.append(" and supplierRel.supplier=:supplierId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != needId){
			query.setParameter("needId" , needId);
		}
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId) ;
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<ProductCategory> findByAllSupplier(Long supplierId) {

		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" where 1=1 and goods.deleted=0 and goods.supplier=:supplierId");

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != supplierId){
			query.setParameter("supplierId" , supplierId);
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	/**
	 * @param supplierRelationId
	 * @return
	 */
	@Override
	public List<ProductCategory> findByAssSupplier(Long supplierRelationId) {
		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_supplier_product assSupplierProduct on product.id=assSupplierProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assSupplierProduct.supply_relation");
		findSql.append(" where 1=1 and goods.is_marketable=true");

		if(null != supplierRelationId){
			findSql.append(" and supplierRel.id=:supplierRelationId");
		}


		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != supplierRelationId){
			query.setParameter("supplierRelationId" , supplierRelationId);
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<ProductCategory> findLike(Long productCategoryId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCategory> criteriaQuery = criteriaBuilder.createQuery(ProductCategory.class);
		Root<ProductCategory> root = criteriaQuery.from(ProductCategory.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (productCategoryId != null) {
			restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("treePath"), "%"+productCategoryId+"%"));
		}
		criteriaQuery.where(restrictions);
		return findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<ProductCategory> findByGradeAndSupplierSuppler(SupplierSupplier supplierSupplier){
		StringBuffer findSql = new StringBuffer(" SELECT CASE SUBSTRING_INDEX(category.tree_path, ',', 2) WHEN ',' THEN category.id ELSE ");
		findSql.append(" substring(SUBSTRING_INDEX(category.tree_path, ',', 2),2) END categoryId ");
		findSql.append(" from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		findSql.append(" LEFT JOIN t_supplier_product supplierProduct on supplierProduct.products=products.id ");
		findSql.append(" where supplierProduct.supply_relation=:supplierSupplier ");
		findSql.append(" GROUP BY categoryId ");
		
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		
		if(supplierSupplier != null){
			query.setParameter("supplierSupplier" , supplierSupplier.getId());
		}

		List<Long> ids = query.getResultList();
		
		StringBuffer findSql2=new StringBuffer("select * from xx_product_category where id in (:ids)");
		Query query2 = entityManager.createNativeQuery(findSql2.toString(),ProductCategory.class);
		query2.setParameter("ids", ids);
		
		return query2.getResultList();
		
	}
	
	@Override
	public List<ProductCategory> findBySupplyNeed(SupplyNeed supplyNeed,Supplier supplier,Supplier cruuSupplier,Boolean hasDistribution){
		StringBuffer findSql = new StringBuffer(" SELECT CASE SUBSTRING_INDEX(category.tree_path, ',', 2) WHEN ',' THEN category.id ELSE ");
		findSql.append(" substring(SUBSTRING_INDEX(category.tree_path, ',', 2),2) END categoryId ");
		findSql.append(" from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_product_category categorySource on categorySource.id=category.source ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");

		if (supplyNeed != null) {
			findSql.append(" LEFT JOIN t_need_product supplierNeedProduct on supplierNeedProduct.products=products.id ");
		}
		findSql.append(" where 1=1  and category.deleted=0 ");
		if (supplyNeed != null) {
			findSql.append(" and supplierNeedProduct.supply_need=:supplyNeed ");
		}
		if (cruuSupplier != null) {
			findSql.append(" and category.supplier=:cruuSupplier ");
		}
		if (supplier != null) {
			findSql.append(" and category.source is not null and categorySource.supplier = :supplier");
		}else {
			if (hasDistribution) {
				findSql.append("and category.source is not null ");
			}
		}

		findSql.append(" GROUP BY categoryId ");
		
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		
		if(supplyNeed != null){
			query.setParameter("supplyNeed" , supplyNeed.getId());
		}
		if (cruuSupplier != null) {
			query.setParameter("cruuSupplier" , cruuSupplier.getId());
		}
		if(supplier != null){
			query.setParameter("supplier" , supplier.getId());
		}

		List<Long> ids = query.getResultList();
		
		StringBuffer findSql2=new StringBuffer("select * from xx_product_category where id in (:ids)");
		Query query2 = entityManager.createNativeQuery(findSql2.toString(),ProductCategory.class);
		query2.setParameter("ids", ids);
		
		return query2.getResultList();
		
	}

	@Override
	public ProductCategory findBySourceAndSupplier(Supplier supplier,
			ProductCategory source , SupplierSupplier supplierSupplier) {
		try {
			StringBuffer findSql = new StringBuffer("select * from xx_product_category productCategory where 1=1");
			if(supplier.getId() != null) {
				findSql.append(" and productCategory.supplier=:supplier");
			}
			if(source.getId() != null) {
				findSql.append(" and productCategory.source=:source");
			}
			if(supplierSupplier.getId() != null) {
				findSql.append(" and productCategory.supplier_supplier=:supplierSupplierId");
			}
			Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;
			if(supplier.getId() != null) {
				query.setParameter("supplier" , supplier.getId());
			}
			if(source.getId() != null) {
				query.setParameter("source" , source.getId()) ;
			}
			if(supplierSupplier.getId() != null) {
				query.setParameter("supplierSupplierId" , supplierSupplier.getId());
			}
			return (ProductCategory) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public List<ProductCategory> findChildrenList(
			ProductCategory productCategory, boolean recursive, Integer count,
			Supplier supplier, String searchName) {
		TypedQuery<ProductCategory> query;
		StringBuffer queryStr = new StringBuffer() ;
		if (recursive) {
			if (productCategory != null) {
				queryStr.append("select productCategory from ProductCategory productCategory where productCategory.deleted=0 and productCategory.treePath like :treePath") ;
				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and productCategory.name like :searchName");
				}
				//queryStr.append(" and productCategory.types=0");
				
				queryStr.append(" order by productCategory.grade asc, productCategory.order asc");

				//String jpql = "select productCategory from ProductCategory productCategory where productCategory.treePath like :treePath order by productCategory.grade asc, productCategory.order asc";

				query = entityManager.createQuery(queryStr.toString(), ProductCategory.class).setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			} else {
				//String jpql = "select productCategory from ProductCategory productCategory order by productCategory.grade asc, productCategory.order asc";

				queryStr.append("select productCategory from ProductCategory productCategory where 1=1 and productCategory.deleted=0") ;

				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and productCategory.name like :searchName");
				}
				queryStr.append(" and productCategory.source is null");
				//queryStr.append(" and productCategory.types=0");

				queryStr.append(" order by productCategory.grade asc, productCategory.order asc");

				query = entityManager.createQuery(queryStr.toString() , ProductCategory.class);

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProductCategory> result = query.getResultList();
			sort(result);
			return result;
		} else {
			//String jpql = "select productCategory from ProductCategory productCategory where productCategory.parent = :parent order by productCategory.order asc";

			queryStr.append("select productCategory from ProductCategory productCategory where 1=1 and  productCategory.deleted=0 and productCategory.parent = :parent") ;

			if(null != supplier){
				queryStr.append(" and supplier=:supplier");
			}
			if(null != searchName) {
				queryStr.append(" and productCategory.name like :searchName");
			}

			queryStr.append(" order by productCategory.grade asc, productCategory.order asc");


			query = entityManager.createQuery(queryStr.toString(), ProductCategory.class).setParameter("parent", productCategory);

			if(null != supplier){
				query.setParameter("supplier" , supplier);
			}
			if(null != searchName) {
				query.setParameter("searchName" , "%" + searchName + "%");
			}

			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}
	
	

	@Override
	public List<ProductCategory> findByGradeAndNeedByFormal(SupplierSupplier supplierSupplier,Need need){
		StringBuffer findSql = new StringBuffer(" SELECT CASE SUBSTRING_INDEX(category.tree_path, ',', 2) WHEN ',' THEN category.id ELSE ");
		findSql.append(" substring(SUBSTRING_INDEX(category.tree_path, ',', 2),2) END categoryId ");
		findSql.append(" from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		if (need != null) {
			findSql.append(" LEFT JOIN t_supplier_need_product supplierNeedProduct on supplierNeedProduct.products=products.id ");
		}else {
			findSql.append(" LEFT JOIN t_supplier_product supplierNeedProduct on supplierNeedProduct.products=products.id ");
		}
		findSql.append(" where supplierNeedProduct.supply_relation=:supplierSupplier ");
		if (need != null) {
			findSql.append(" and supplierNeedProduct.need=:need ");
		}
		findSql.append(" GROUP BY categoryId ");
		
		Query query = entityManager.createNativeQuery(findSql.toString()) ;
		
		if(supplierSupplier != null){
			query.setParameter("supplierSupplier" , supplierSupplier.getId());
		}
		if(need != null){
			query.setParameter("need" , need.getId());
		}

		List<Long> ids = query.getResultList();
		
		StringBuffer findSql2=new StringBuffer("select * from xx_product_category where id in (:ids)");
		Query query2 = entityManager.createNativeQuery(findSql2.toString(),ProductCategory.class);
		query2.setParameter("ids", ids);
		
		return query2.getResultList();
		
	}
	
	/**
	 * 非自营
	 */
	
	@Override
	public List<ProductCategory> findByAssSupplier(Long supplierId, Long bySupplierId) {

		StringBuffer findSql = new StringBuffer("select distinct category.id , category.* from xx_product_category category");
		findSql.append(" inner join xx_goods goods on goods.product_category=category.id");
		findSql.append(" inner join xx_product product on goods.id=product.goods");
		findSql.append(" inner join t_supplier_product assSupplierProduct on product.id=assSupplierProduct.products");
		findSql.append(" inner join t_supplier_supplier supplierRel on supplierRel.id=assSupplierProduct.supply_relation");
		findSql.append(" where 1=1 and goods.is_marketable=true and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		findSql.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());

		if(null != supplierId){
			findSql.append(" and supplierRel.supplier=:supplierId");
		}

		if(null != bySupplierId){
			findSql.append(" and supplierRel.by_supplier=:bySupplierId");
		}

		Query query = entityManager.createNativeQuery(findSql.toString() , ProductCategory.class) ;

		if(null != supplierId){
			query.setParameter("supplierId" , supplierId);
		}
		if(null != bySupplierId){
			query.setParameter("bySupplierId" , bySupplierId) ;
		}

		List<ProductCategory> result = query.getResultList() ;

		return result;
	}

	@Override
	public List<ProductCategory> findByParent(Supplier supplier, ProductCategory parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.supplier =:supplier and productCategory.deleted=0 and productCategory.parent =:parent and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("parent", parent).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			} else {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.supplier =:supplier and productCategory.deleted=0 and productCategory.parent is null and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<ProductCategory> findByParent(Member member, ProductCategory parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.member =:member and productCategory.deleted=0 and productCategory.parent =:parent and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("parent", parent).setParameter("member", member).setParameter("name", name).getResultList();
			} else {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.member =:member and productCategory.deleted=0 and productCategory.parent is null and productCategory.name =:name";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("member", member).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ProductCategory> findTree(Member member, String searchName, ProductCategory productCategory) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from xx_product_category productCategory where 1=1 and productCategory.types = 1");
		if(null != member) {
			buffer.append(" and productCategory.member = :member");
		}
		if(null != productCategory) {
			buffer.append(" and productCategory.tree_path like :treePath");
		}
		if(null != searchName) {
			buffer.append(" and productCategory.name like :searchName");
		}
		buffer.append(" and productCategory.deleted = 0");
		buffer.append(" order by productCategory.grade ASC, productCategory.orders ASC");
		
		Query query = entityManager.createNativeQuery(buffer.toString(), ProductCategory.class);
		if(null != member) {
			query.setParameter("member", member.getId());
		}
		if(null != productCategory) {
			query.setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
		}
		if(null != searchName) {
			query.setParameter("searchName", "%" + searchName + "%");
		}
		List<ProductCategory> result = query.getResultList();
		sort(result);
		return result;
	}

	@Override
	public List<ProductCategory> findLocalChildren(ProductCategory productCategory, boolean recursive, Integer count) {
		TypedQuery<ProductCategory> query;
		if (recursive) {
			if (productCategory != null) {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.types=1 and productCategory.deleted=0 and productCategory.treePath like :treePath order by productCategory.grade asc, productCategory.order asc";
				query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("treePath", "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%");
			} else {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.types=1 and productCategory.deleted=0 and order by productCategory.grade asc, productCategory.order asc";
				query = entityManager.createQuery(jpql, ProductCategory.class);
			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProductCategory> result = query.getResultList();
			sort(result);
			return result;
		} else {
			String jpql = "select productCategory from ProductCategory productCategory where productCategory.types=1 and productCategory.deleted=0 and productCategory.parent = :parent order by productCategory.order asc";
			query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("parent", productCategory);
			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}

	@Override
	public List<ProductCategory> findBySupplierSuppler(Long supplierSupplierId) {
		StringBuffer findSql = new StringBuffer(" SELECT category.* from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		findSql.append(" LEFT JOIN t_supplier_product supplierProduct on supplierProduct.products=products.id ");
		findSql.append(" where supplierProduct.supply_relation=:supplierSupplierId and goods.deleted=0");

		Query query = entityManager.createNativeQuery(findSql.toString(),ProductCategory.class) ;

		if(supplierSupplierId != null){
			query.setParameter("supplierSupplierId" , supplierSupplierId);
		}
		return query.getResultList();

	}

	@Override
	public List<ProductCategory> findBySupplyNeedId(Long supplyNeedId) {
		StringBuffer findSql = new StringBuffer(" SELECT category.* from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		findSql.append(" LEFT JOIN t_need_product needProduct on needProduct.products=products.id ");
		findSql.append(" where needProduct.supply_need=:supplyNeedId  and goods.deleted=0 ");
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		Query query = entityManager.createNativeQuery(findSql.toString(),ProductCategory.class) ;

		if(supplyNeedId != null){
			query.setParameter("supplyNeedId" , supplyNeedId);
		}

		return query.getResultList();
	}

	@Override
	public List<ProductCategory> findRootByMember(Member member) {
		String jpql = "select productCategory from ProductCategory productCategory where  productCategory.member = :member and productCategory.parent is null order by productCategory.order asc";
		TypedQuery<ProductCategory> query = entityManager.createQuery(jpql, ProductCategory.class).setParameter("member" , member);
		return query.getResultList();
	}

	@Override
	public List<ProductCategory> findBySupplierSupplerNeed(Long supplierSupplierId, Need need) {
		StringBuffer findSql = new StringBuffer(" SELECT category.* from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		findSql.append(" LEFT JOIN t_supplier_need_product supplierNeedProduct on supplierNeedProduct.products=products.id ");
		findSql.append(" where 1=1 and goods.deleted=0 ");
		if(supplierSupplierId != null){
			findSql.append(" and supplierNeedProduct.supply_relation=:supplierSupplierId ");
		}
		if(need != null){
			findSql.append(" and supplierNeedProduct.need=:need ");
		}

		Query query = entityManager.createNativeQuery(findSql.toString(),ProductCategory.class) ;

		if(supplierSupplierId != null){
			query.setParameter("supplierSupplierId" , supplierSupplierId);
		}
		if(need != null){
			query.setParameter("need" , need.getId());
		}
		return query.getResultList();

	}

	@Override
	public List<ProductCategory> findBySupplyNeedShop(Long supplyNeedId, Long shopId,Long supplierId) {
		StringBuffer findSql = new StringBuffer(" SELECT category.* from xx_product_category category ");
		findSql.append(" LEFT JOIN xx_goods goods on goods.product_category=category.id ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" LEFT JOIN xx_product products on products.goods=goods.id ");
		findSql.append(" LEFT JOIN t_need_shop_product needShopProduct on needShopProduct.products=products.id ");
		findSql.append(" where 1=1 and goods.deleted=0");
		if(supplyNeedId != null){
			findSql.append(" and needShopProduct.supply_need=:supplyNeedId ");
		}
		if(shopId != null){
			findSql.append(" and needShopProduct.shop=:shop ");
		}
		if(supplierId != null){
			findSql.append(" and needShopProduct.supplier=:supplier ");
		}
		findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");

		Query query = entityManager.createNativeQuery(findSql.toString(),ProductCategory.class) ;

		if(supplyNeedId != null){
			query.setParameter("supplyNeedId" , supplyNeedId);
		}

		if (shopId != null){
			query.setParameter("shop" , shopId);
		}
		if(supplierId != null){
			query.setParameter("supplier" , supplierId);
		}

		return query.getResultList();
	}

	@Override
	public List<ProductCategory> findByParent(Member member,
			ProductCategory parent, String name, Types types) {
		try {
			if (parent != null) {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.member =:member and productCategory.deleted=0 and productCategory.parent =:parent and productCategory.name =:name and productCategory.types=:types";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("parent", parent).setParameter("member", member).setParameter("name", name).setParameter("types", types).getResultList();
			} else {
				String jpql = "select productCategory from ProductCategory productCategory where productCategory.member =:member and productCategory.deleted=0 and productCategory.parent is null and productCategory.name =:name and productCategory.types=:types";
				return entityManager.createQuery(jpql, ProductCategory.class).setParameter("member", member).setParameter("name", name).setParameter("types", types).getResultList();
			}
		} catch (Exception e) {
			return null;
		}
	}

}
package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.entity.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.SupplierNeedProductDao;

@Repository("supplierNeedProductDaoImpl")
public class SupplierNeedProductDaoImpl extends BaseDaoImpl<SupplierNeedProduct, Long> implements SupplierNeedProductDao{

	@Override
	public List<SupplierNeedProduct> findBySupplier(SupplierSupplier supplierSupplier, Need need) {
		StringBuffer findSql = new StringBuffer("select supplierNeedProduct.* from t_supplier_need_product supplierNeedProduct ");
		findSql.append(" left join xx_product product on product.id=supplierNeedProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
        findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
        findSql.append(" where 1=1 and goods.deleted=0 ");
        if (supplierSupplier != null) {
			findSql.append(" and supplierNeedProduct.supply_relation = :supplierSupplier");
		}
		if (need != null) {
			findSql.append(" and supplierNeedProduct.need = :need");
		}
        findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
        
        Query query = entityManager.createNativeQuery(findSql.toString(),SupplierNeedProduct.class);
        if (supplierSupplier != null) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		if (need != null) {
			query.setParameter("need", need.getId());
		}
        return query.getResultList();
	}

	@Override
	public SupplierNeedProduct findSupplierNeedProduct(SupplierAssignRelation supplierAssignRelation, Need need,
			Product product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierNeedProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierNeedProduct.class);
		Root<SupplierNeedProduct> root = criteriaQuery.from(SupplierNeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assignRelation"), supplierAssignRelation));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need"), need));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
		criteriaQuery.where(restrictions);
		List<SupplierNeedProduct> list = super.findList(criteriaQuery, null, null, null, null);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public SupplierNeedProduct findSupplierNeedProduct(SupplierSupplier supplierSupplier, Need need, Product product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierNeedProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierNeedProduct.class);
		Root<SupplierNeedProduct> root = criteriaQuery.from(SupplierNeedProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need"), need));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
		criteriaQuery.where(restrictions);
		List<SupplierNeedProduct> list = super.findList(criteriaQuery, null, null, null, null);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Page<SupplierNeedProduct> findBySupplierPage(SupplierSupplier supplierSupplier, Need need, String goodsName, Long productCategoryId, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select supplierNeedProduct.* from t_supplier_need_product supplierNeedProduct ");
		findSql.append(" left join xx_product product on product.id=supplierNeedProduct.products ");
		findSql.append(" left join xx_goods goods on goods.id=product.goods ");
		findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
		findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
		findSql.append(" where 1=1 and goods.deleted=0 ");
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

		Query query = entityManager.createNativeQuery(findSql.toString(),SupplierNeedProduct.class);
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

		return new Page<SupplierNeedProduct>(query.getResultList(), total, pageable);
	}

	@Override
	public List<SupplierNeedProduct> findBySupplier(SupplierSupplier supplierSupplier, Need need, String goodsName, ProductCategory productCategory) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierNeedProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierNeedProduct.class);
		Root<SupplierNeedProduct> root = criteriaQuery.from(SupplierNeedProduct.class);
		criteriaQuery.select(root);
		Join<SupplierNeedProduct, Product> ProJoin = root.join("products" , JoinType.LEFT);
		Join<Product, Goods> goodsJoin = ProJoin.join("goods" , JoinType.LEFT);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplierSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		}
		if (need != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need"), need));
		}
		if (!StringUtils.isEmpty(goodsName)){
	        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(goodsJoin.<String>get("name"), "%"+goodsName+"%"));
	    }
	    if (productCategory != null) {
	        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(goodsJoin.get("productCategory"), productCategory));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<SupplierNeedProduct> findBySupplierBarCode(SupplierSupplier supplierSupplier, Need need, String barCode) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierNeedProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierNeedProduct.class);
		Root<SupplierNeedProduct> root = criteriaQuery.from(SupplierNeedProduct.class);
		criteriaQuery.select(root);
		Join<SupplierNeedProduct, Product> ProJoin = root.join("products" , JoinType.LEFT);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplierSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		}
		if (need != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("need"), need));
		}
		if (!StringUtils.isEmpty(barCode)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(ProJoin.get("barCode"), barCode));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<SupplierNeedProduct> getProduct(Product products, Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierNeedProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierNeedProduct.class);
		Root<SupplierNeedProduct> root = criteriaQuery.from(SupplierNeedProduct.class);
		Join<SupplierNeedProduct, Need> needJoin = root.join("need" , JoinType.LEFT);
		Join<Need, Shop> shopJoin = needJoin.join("shops" , JoinType.LEFT);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (products != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), products));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(shopJoin.get("member"), member));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

}

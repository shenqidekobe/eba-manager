package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedShopProductDao;
import com.microBusiness.manage.entity.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: pengtianwen
 * @create: 2018-03-08 13:54
 **/
@Repository("needShopProductDaoImpl")
public class NeedShopProductDaoImpl extends BaseDaoImpl<NeedShopProduct,Long> implements NeedShopProductDao {

    @Override
    public List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier) {
        StringBuffer findSql = new StringBuffer(" select needShopProduct.* from t_need_shop_product needShopProduct ");
        findSql.append(" left join xx_product product on product.id=needShopProduct.products ");
        findSql.append(" left join xx_goods goods on goods.id=product.goods ");
        findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
        findSql.append(" where 1=1 and goods.deleted=0 ");
        if (supplyNeed != null) {
            findSql.append(" and needShopProduct.supply_need = :supplyNeed");
        }
        if (shop != null) {
            findSql.append(" and needShopProduct.shop = :shop");
        }
        if (supplier != null) {
            findSql.append(" and needShopProduct.supplier = :supplier");
        }
        
        findSql.append(" and (supplierSupplier.id is null or (supplierSupplier.status=1 and supplierSupplier.start_date<=now() and now() <= supplierSupplier.end_date))");
        
        Query query = entityManager.createNativeQuery(findSql.toString(),NeedShopProduct.class);
        if (supplyNeed != null) {
            query.setParameter("supplyNeed", supplyNeed.getId());
        }
        if (shop != null) {
            query.setParameter("shop", shop.getId());
        }
        if(supplier != null) {
        	query.setParameter("supplier", supplier.getId());
        }
        return query.getResultList();
    }

    @Override
    public NeedShopProduct getNeedShopProduct(SupplyNeed supplyNeed, Shop shop, Supplier supplier, Product product) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedShopProduct> criteriaQuery = criteriaBuilder.createQuery(NeedShopProduct.class);
        Root<NeedShopProduct> root = criteriaQuery.from(NeedShopProduct.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (supplyNeed != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed"), supplyNeed));
        }
        if (shop != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
        }
        if (supplier != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        if (product != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
        }
        criteriaQuery.where(restrictions);
        List<NeedShopProduct> list=super.findList(criteriaQuery, null, null, null, null);
        if (list != null && list.size() >0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<NeedShopProduct> getPage(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName,Long productCategoryId, Pageable pageable) {
        StringBuffer findSql = new StringBuffer("select needShopProduct.* from t_need_shop_product needShopProduct ");
        findSql.append(" left join xx_product product on product.id=needShopProduct.products ");
        findSql.append(" left join xx_goods goods on goods.id=product.goods ");
        findSql.append(" left join t_supplier_supplier supplierSupplier on supplierSupplier.id=goods.supplier_supplier ");
        findSql.append(" left join xx_product_category category on category.id=goods.product_category ");
        findSql.append(" where 1=1 and goods.deleted=0 ");
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

        Query query = entityManager.createNativeQuery(findSql.toString(),NeedShopProduct.class);
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

        return new Page<NeedShopProduct>(query.getResultList(), total, pageable);
    }

	@Override
	public List<NeedShopProduct> getList(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String goodsName, ProductCategory productCategory) {
		 CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaQuery<NeedShopProduct> criteriaQuery = criteriaBuilder.createQuery(NeedShopProduct.class);
	        Root<NeedShopProduct> root = criteriaQuery.from(NeedShopProduct.class);
	        Join<NeedShopProduct, Product> ProJoin = root.join("products" , JoinType.LEFT);
			Join<Product, Goods> goodsJoin = ProJoin.join("goods" , JoinType.LEFT);
	        criteriaQuery.select(root);
	        Predicate restrictions = criteriaBuilder.conjunction();
	        if (supplyNeed != null){
	            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed"), supplyNeed));
	        }
	        if (shop != null){
	            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
	        }
	        if (supplier != null){
	            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
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
	public List<NeedShopProduct> getList() {
		try {
			String jpql = "select needShopProduct from NeedShopProduct needShopProduct";
			return entityManager.createQuery(jpql, NeedShopProduct.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<NeedShopProduct> getByBarCode(SupplyNeed supplyNeed, Shop shop, Supplier supplier, String barCode) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedShopProduct> criteriaQuery = criteriaBuilder.createQuery(NeedShopProduct.class);
        Root<NeedShopProduct> root = criteriaQuery.from(NeedShopProduct.class);
        Join<NeedShopProduct, Product> ProJoin = root.join("products" , JoinType.LEFT);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (supplyNeed != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyNeed"), supplyNeed));
        }
        if (shop != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
        }
        if (supplier != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }
        if (!StringUtils.isEmpty(barCode)) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(ProJoin.get("barCode"), barCode));
		}
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<NeedShopProduct> getProduct(Shop shop, Product products, Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedShopProduct> criteriaQuery = criteriaBuilder.createQuery(NeedShopProduct.class);
        Root<NeedShopProduct> root = criteriaQuery.from(NeedShopProduct.class);
        Join<NeedShopProduct, Shop> shopJoin = root.join("shop" , JoinType.LEFT);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (shop != null){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
        }
        if (products != null) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), products));
		}
        if (member != null) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(shopJoin.get("member"), member));
		}
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public List<NeedShopProduct> getNeedShopProductList(Shop shop, Supplier supplier, Product product) {
		try {
			String jpql = "select needShopProduct from NeedShopProduct needShopProduct where needShopProduct.shop=:shop and needShopProduct.supplier=:supplier and needShopProduct.products=:products";
			return entityManager.createQuery(jpql, NeedShopProduct.class).setParameter("shop", shop)
					.setParameter("supplier", supplier).setParameter("products", product).getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}
}

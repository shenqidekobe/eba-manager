package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SupplierProductDao;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierProduct;
import com.microBusiness.manage.entity.SupplierSupplier;

@Repository("supplierProductDaoImpl")
public class SupplierProductDaoImpl extends BaseDaoImpl<SupplierProduct, Long> implements SupplierProductDao{
    @Override
    public SupplierProduct getProduct(Product product, Supplier supplier, Supplier bySupplier, SupplierSupplier.Status status, Need need) {
        StringBuffer findSql = new StringBuffer();
        findSql.append("select supplierPro.* from t_supplier_product supplierPro ");
        findSql.append(" inner join t_supplier_supplier supplyRelation on supplierPro.supply_relation=supplyRelation.id");

        findSql.append(" where 1=1");
        findSql.append(" and supplyRelation.status=:status");
        findSql.append(" and supplyRelation.supplier=:supplier");
        findSql.append(" and supplyRelation.by_supplier=:bySupplier");
        findSql.append(" and supplierPro.products=:products");


        Query query = entityManager.createNativeQuery(findSql.toString() , SupplierProduct.class);

        query.setParameter("status" , status.ordinal());
        query.setParameter("supplier" , supplier.getId()) ;
        query.setParameter("bySupplier" , bySupplier.getId());
        query.setParameter("products" , product.getId());

        SupplierProduct supplierProduct = (SupplierProduct) query.getSingleResult();

        return supplierProduct;
    }

    @Override
    public SupplierProduct getProduct(Long productId, Long supplierId, Long bySupplierId, SupplierSupplier.Status status, Long needId) {
        StringBuffer findSql = new StringBuffer();
        findSql.append("select supplierPro.* from t_supplier_product supplierPro ");
        findSql.append(" inner join t_supplier_supplier supplyRelation on supplierPro.supply_relation=supplyRelation.id");

        findSql.append(" where 1=1");
        findSql.append(" and supplyRelation.status=:status");
        findSql.append(" and supplyRelation.supplier=:supplier");
        findSql.append(" and supplyRelation.by_supplier=:bySupplier");
        findSql.append(" and supplierPro.products=:products");


        Query query = entityManager.createNativeQuery(findSql.toString() , SupplierProduct.class);

        query.setParameter("status" , status.ordinal());
        query.setParameter("supplier" , supplierId) ;
        query.setParameter("bySupplier" , bySupplierId);
        query.setParameter("products" , productId);


        try {
            SupplierProduct supplierProduct = (SupplierProduct) query.getSingleResult();
            return supplierProduct;
        } catch (NoResultException e) {
            return null;
        }

    }

	@Override
	public SupplierProduct getSupplierProduct(
			SupplierSupplier supplierSupplier, Product product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierProduct.class);
		Root<SupplierProduct> root = criteriaQuery.from(SupplierProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplierSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		}
		if(product != null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<SupplierProduct> query = entityManager.createQuery(criteriaQuery);
		List<SupplierProduct> supplierProduct = query.getResultList();
		if(supplierProduct != null && !supplierProduct.isEmpty()){
			return supplierProduct.get(0);
		}
		return null;
	}

	@Override
	public SupplierProduct findSupplierProduct(SupplierSupplier supplierSupplier, Product product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierProduct.class);
		Root<SupplierProduct> root = criteriaQuery.from(SupplierProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (supplierSupplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplyRelation"), supplierSupplier));
		}
		if(product != null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<SupplierProduct> query = entityManager.createQuery(criteriaQuery);
		List<SupplierProduct> supplierProduct = query.getResultList();
		if(supplierProduct != null && !supplierProduct.isEmpty()){
			return supplierProduct.get(0);
		}
		return null;
	}

	@Override
	public Page<SupplierProduct> findPage(Pageable pageable,
			Long productCategoryId, SupplierSupplier supplierSupplier) {
		try {
			String searchValue=pageable.getSearchValue();
			StringBuffer buffer = new StringBuffer("select supplierProduct.* from t_supplier_supplier supplierSupplier");
			buffer.append(" INNER JOIN t_supplier_product supplierProduct on supplierSupplier.id = supplierProduct.supply_relation");
			buffer.append(" INNER JOIN xx_product product on supplierProduct.products = product.id");
			buffer.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1");
			if(null != supplierSupplier) {
				buffer.append(" and supplierSupplier.id = :id");
			}
			if(null != searchValue) {
				buffer.append(" and goods.name like :searchValue");
			}
			if(null != productCategoryId) {
				buffer.append(" and goods.product_category = :productCategoryId");
			}
			buffer.append(" and supplierProduct.deleted = 0 ");
			
			StringBuffer countSql = new StringBuffer("select count(*) from t_supplier_supplier supplierSupplier");
			countSql.append(" INNER JOIN t_supplier_product supplierProduct on supplierSupplier.id = supplierProduct.supply_relation");
			countSql.append(" INNER JOIN xx_product product on supplierProduct.products = product.id");
			countSql.append(" INNER JOIN xx_goods goods on product.goods = goods.id where 1=1");
			if(null != supplierSupplier) {
				countSql.append(" and supplierSupplier.id = :id");
			}
			if(null != searchValue) {
				countSql.append(" and goods.name like :searchValue");
			}
			if(null != productCategoryId) {
				countSql.append(" and goods.product_category = :productCategoryId");
			}
			countSql.append(" and supplierProduct.deleted = 0");
			
			Query query = entityManager.createNativeQuery(buffer.toString(), SupplierProduct.class);
			Query count = entityManager.createNativeQuery(countSql.toString());
			
			if(null != supplierSupplier) {
				query.setParameter("id", supplierSupplier.getId());
				count.setParameter("id", supplierSupplier.getId());
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
			List<SupplierProduct> list = query.getResultList();
			return new Page<SupplierProduct>(list, total, pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SupplierProduct> getSupplierProductList(
			SupplierSupplier supplierSupplier) {
		StringBuffer buffer = new StringBuffer("select supplierProduct.* from t_supplier_supplier supplierSupplier");
		buffer.append(" INNER JOIN t_supplier_product supplierProduct on supplierSupplier.id = supplierProduct.supply_relation");
		buffer.append(" INNER JOIN xx_product product on supplierProduct.products = product.id where 1=1");
		buffer.append(" and supplierSupplier.id = :id");
		buffer.append(" and supplierProduct.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString(), SupplierProduct.class);
		query.setParameter("id", supplierSupplier.getId());
		return query.getResultList();
	}

	@Override
	public List<SupplierProduct> supplierProductList(
			SupplierSupplier supplierSupplier, List<Long> productIds) {
		StringBuffer buffer = new StringBuffer("select * from t_supplier_product supplierProduct where 1=1");
		if(null != supplierSupplier) {
			buffer.append(" and supplierProduct.supply_relation = :supplierSupplier");
		}
		if(productIds.size() >0) {
			buffer.append(" and supplierProduct.products in(:productIds)");
		}
		Query query = entityManager.createNativeQuery(buffer.toString(), SupplierProduct.class);
		if(null != supplierSupplier) {
			query.setParameter("supplierSupplier", supplierSupplier.getId());
		}
		if(productIds.size() >0) {
			query.setParameter("productIds", productIds);
		}
		return query.getResultList();
	}

	@Override
	public List<SupplierProduct> findByProduct(Product product) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<SupplierProduct> criteriaQuery = criteriaBuilder.createQuery(SupplierProduct.class);
		Root<SupplierProduct> root = criteriaQuery.from(SupplierProduct.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(product != null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("products"), product));
		}
		Join<SupplierProduct , SupplierSupplier> supplierJoin = root.join("supplyRelation" , JoinType.LEFT);
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(supplierJoin.get("status"), SupplierSupplier.Status.toBeConfirmed),criteriaBuilder.equal(supplierJoin.get("status"), SupplierSupplier.Status.inTheSupply)));
		criteriaQuery.where(restrictions);
		TypedQuery<SupplierProduct> query = entityManager.createQuery(criteriaQuery);
		List<SupplierProduct> supplierProduct = query.getResultList();
		return supplierProduct;
	}

}

package com.microBusiness.manage.dao.impl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.CompanyGoodsDao;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.Supplier;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("companyGoodsDaoImpl")
public class CompanyGoodsDaoImpl extends BaseDaoImpl<CompanyGoods, Long> implements CompanyGoodsDao {

	@Override
	public Page<CompanyGoods> query(CompanyGoods companyGoods, Boolean priceBoolean, Boolean popularityBoolean, Boolean packagesNumBoolean,  Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CompanyGoods> criteriaQuery = criteriaBuilder.createQuery(CompanyGoods.class);
		Root<CompanyGoods> root = criteriaQuery.from(CompanyGoods.class);
		criteriaQuery.select(root);
		Join<CompanyGoods , Category> companyGoodCategoryJoin = root.join("category" , JoinType.LEFT);
		Join<CompanyGoods , Supplier> companyGoodSupplierJoin = root.join("supplier" , JoinType.LEFT);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (companyGoods.getName()!=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.and(criteriaBuilder.like(root.<String>get("name"), "%"+companyGoods.getName()+"%")));
		}
		if (companyGoods.getSupplier() !=null) {
			if (companyGoods.getId() != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.and(criteriaBuilder.notEqual(root.get("id"), companyGoods.getId())));
			}
			
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.and(criteriaBuilder.equal(companyGoodSupplierJoin.get("id"), companyGoods.getSupplier().getId())));
		}
		if (companyGoods.getCategory() !=null) {
			if (companyGoods.getCategory().getId() !=null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(companyGoodCategoryJoin.<String>get("treePath") , "%"+companyGoods.getCategory().getId()+"%"),criteriaBuilder.equal(companyGoodCategoryJoin.get("id"), companyGoods.getCategory().getId())));
			}
		}
		if(companyGoods.getPubType() == CompanyGoods.PubType.pub_supply) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("pubType"), CompanyGoods.PubType.pub_supply));
			if (priceBoolean != null) {
				if (priceBoolean) {
					pageable.setOrderProperty("marketPrice");
					pageable.setOrderDirection(Order.Direction.asc);
				}else {
					pageable.setOrderProperty("marketPrice");
					pageable.setOrderDirection(Order.Direction.desc);
				}
			}
		}
		if(companyGoods.getPubType() == CompanyGoods.PubType.pub_need) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("pubType"), CompanyGoods.PubType.pub_need));
			if (packagesNumBoolean != null) {
				if (packagesNumBoolean) {
					pageable.setOrderProperty("needNum");
					pageable.setOrderDirection(Order.Direction.asc);
				}else {
					pageable.setOrderProperty("needNum");
					pageable.setOrderDirection(Order.Direction.desc);
				}
			}
		}
		if (popularityBoolean != null) {
			if (popularityBoolean) {
				pageable.setOrderProperty("hits");
				pageable.setOrderDirection(Order.Direction.asc);
			}else {
				pageable.setOrderProperty("hits");
				pageable.setOrderDirection(Order.Direction.desc);
			}
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), CompanyGoods.Status.status_ok));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}


	/**
	 * @param pageable
	 * @param category
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	@Override
	public Page<CompanyGoods> findPage(Pageable pageable, Category category, CompanyGoods.Status status, String searchValue , CompanyGoods.PubType pubType) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyGoods> criteriaQuery = criteriaBuilder.createQuery(CompanyGoods.class);
        Root<CompanyGoods> root = criteriaQuery.from(CompanyGoods.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("delflag") , CompanyGoods.Delflag.delflag_no));

		if(StringUtils.isNotEmpty(searchValue)){
			Join<CompanyGoods , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT) ;
			restrictions = criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name") , "%"+ searchValue+"%") , criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+ searchValue+"%")));
		}

        if (null != category) {
            Subquery<Category> subquery = criteriaQuery.subquery(Category.class);
            Root<Category> subqueryRoot = subquery.from(Category.class);
            subquery.select(subqueryRoot);

            subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, category), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + Category.TREE_PATH_SEPARATOR + category.getId() + Category.TREE_PATH_SEPARATOR + "%")));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("category")).value(subquery));
        }

        if(null != status){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , status));
        }

        if(null != pubType){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("pubType") , pubType));
        }
        criteriaQuery.where(restrictions);

		return super.findPage(criteriaQuery , pageable);
	}

	/**
	 * @param pageable
	 * @param category
	 * @param status
	 * @param searchValue
	 * @param pubType
	 * @return
	 */
	@Override
	public List<CompanyGoods> findList(Pageable pageable, Category category, CompanyGoods.Status status, String searchValue, CompanyGoods.PubType pubType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CompanyGoods> criteriaQuery = criteriaBuilder.createQuery(CompanyGoods.class);
		Root<CompanyGoods> root = criteriaQuery.from(CompanyGoods.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("delflag") , CompanyGoods.Delflag.delflag_no));

		if(StringUtils.isNotEmpty(searchValue)){
			Join<CompanyGoods , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT) ;
			restrictions = criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name") , "%"+ searchValue+"%") , criteriaBuilder.like(supplierJoin.<String>get("name"), "%"+ searchValue+"%")));
		}

		if (null != category) {
			Subquery<Category> subquery = criteriaQuery.subquery(Category.class);
			Root<Category> subqueryRoot = subquery.from(Category.class);
			subquery.select(subqueryRoot);

			subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, category), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + Category.TREE_PATH_SEPARATOR + category.getId() + Category.TREE_PATH_SEPARATOR + "%")));

			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("category")).value(subquery));
		}

		if(null != status){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status") , status));
		}

		if(null != pubType){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("pubType") , pubType));
		}
		criteriaQuery.where(restrictions);
		TypedQuery<CompanyGoods> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}
}
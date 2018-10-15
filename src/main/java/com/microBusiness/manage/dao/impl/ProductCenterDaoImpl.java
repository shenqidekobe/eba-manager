package com.microBusiness.manage.dao.impl;

import java.util.List;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ProductCenterDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.GoodsCenter;
import com.microBusiness.manage.entity.ProductCenter;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:46
 * Describe:
 * Update:
 */
@Repository
public class ProductCenterDaoImpl extends BaseDaoImpl<ProductCenter, Long> implements ProductCenterDao {

    @Override
    public boolean snExists(String sn) {

        if (StringUtils.isEmpty(sn)) {
            return false;
        }

        String jpql = "select count(*) from ProductCenter productCenter where lower(productCenter.sn) = lower(:sn)";
        Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
        return count > 0;
    }


    @Override
    public Page<ProductCenter> findPage(CategoryCenter categoryCenter, String goodsName, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductCenter> criteriaQuery = criteriaBuilder.createQuery(ProductCenter.class);
        Root<ProductCenter> root = criteriaQuery.from(ProductCenter.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        Join<ProductCenter , GoodsCenter> goodsCenterJoin = root.join("goodsCenter" , JoinType.LEFT);

        if(StringUtils.isNotEmpty(goodsName)){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(goodsCenterJoin.<String>get("name"), "%"+goodsName+"%"));
        }

        if(null != categoryCenter){
            Join<GoodsCenter , CategoryCenter> categoryCenterJoin = goodsCenterJoin.join("categoryCenter" , JoinType.LEFT);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(categoryCenterJoin.get("categoryCenter"), categoryCenter));
        }

        criteriaQuery.where(restrictions);

        return super.findPage(criteriaQuery , pageable);
    }


	@Override
	public List<ProductCenter> findList(CategoryCenter categoryCenter,
			String goodsName, String barCode) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCenter> criteriaQuery = criteriaBuilder.createQuery(ProductCenter.class);
		Root<ProductCenter> root = criteriaQuery.from(ProductCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		Join<ProductCenter , GoodsCenter> goodsCenterJoin = root.join("goodsCenter" , JoinType.LEFT);
		
		if(StringUtils.isNotEmpty(goodsName)){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(goodsCenterJoin.<String>get("name"), "%"+goodsName+"%"));
        }

        if(null != categoryCenter){
            Join<GoodsCenter , CategoryCenter> categoryCenterJoin = goodsCenterJoin.join("categoryCenter" , JoinType.LEFT);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(categoryCenterJoin.get("categoryCenter"), categoryCenter));
        }
        if(StringUtils.isNotEmpty(barCode)) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("barCode"), barCode));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}


	@Override
	public List<ProductCenter> findList(CategoryCenter categoryCenter,
			String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProductCenter> criteriaQuery = criteriaBuilder.createQuery(ProductCenter.class);
		Root<ProductCenter> root = criteriaQuery.from(ProductCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		Join<ProductCenter , GoodsCenter> goodsCenterJoin = root.join("goodsCenter" , JoinType.LEFT);
		
		if(StringUtils.isNotEmpty(searchName)){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("barCode") , "%"+searchName+"%") , criteriaBuilder.like(goodsCenterJoin.<String>get("name"), "%"+searchName+"%")));
        }
        if(null != categoryCenter){
            Join<GoodsCenter , CategoryCenter> categoryCenterJoin = goodsCenterJoin.join("categoryCenter" , JoinType.LEFT);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(categoryCenterJoin.get("categoryCenter"), categoryCenter));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
        criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
}

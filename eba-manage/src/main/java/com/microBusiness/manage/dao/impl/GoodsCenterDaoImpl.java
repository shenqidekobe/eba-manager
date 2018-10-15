package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.dao.GoodsCenterDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:25
 * Describe:
 * Update:
 */
@Repository
public class GoodsCenterDaoImpl extends BaseDaoImpl<GoodsCenter, Long> implements GoodsCenterDao {
    @Override
    public Page<GoodsCenter> findPage(GoodsCenter.Type type, CategoryCenter productCategory, Boolean isMarketable, Boolean isList, Boolean isTop, GoodsCenter.OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsCenter> criteriaQuery = criteriaBuilder.createQuery(GoodsCenter.class);
        Root<GoodsCenter> root = criteriaQuery.from(GoodsCenter.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }
        if (productCategory != null) {
            Subquery<CategoryCenter> subquery = criteriaQuery.subquery(CategoryCenter.class);
            Root<CategoryCenter> subqueryRoot = subquery.from(CategoryCenter.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot, productCategory), criteriaBuilder.like(subqueryRoot.<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("categoryCenter")).value(subquery));
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


    @Override
    public boolean snExists(String sn) {

        if (StringUtils.isEmpty(sn)) {
            return false;
        }

        String jpql = "select count(*) from GoodsCenter goodsCenter where lower(goodsCenter.sn) = lower(:sn)";

        Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean findByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }

        String jpql = "select count(*) from GoodsCenter goodsCenter where lower(goodsCenter.name) = lower(:name)";

        Long count = entityManager.createQuery(jpql, Long.class).setParameter("name", name).getSingleResult();
        return count > 0;
    }


	@Override
	public GoodsCenter findBySn(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return null;
		}

		String jpql = "select goods from GoodsCenter goods where lower(goods.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, GoodsCenter.class).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}

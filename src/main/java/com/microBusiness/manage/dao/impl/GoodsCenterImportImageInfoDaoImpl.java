package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsCenterImportImageInfoDao;
import com.microBusiness.manage.entity.GoodsCenterImportImageInfo;

import org.springframework.stereotype.Repository;

@Repository("goodsCenterImportImageInfoDaoImpl")
public class GoodsCenterImportImageInfoDaoImpl extends BaseDaoImpl<GoodsCenterImportImageInfo, Long> implements GoodsCenterImportImageInfoDao {

    @Override
    public Page<GoodsCenterImportImageInfo> findPage(Pageable pageable, String batch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsCenterImportImageInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsCenterImportImageInfo.class);
        Root<GoodsCenterImportImageInfo> root = criteriaQuery.from(GoodsCenterImportImageInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != batch) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("batch"), batch));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    @Override
    public List<GoodsCenterImportImageInfo> findList(String batch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsCenterImportImageInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsCenterImportImageInfo.class);
        Root<GoodsCenterImportImageInfo> root = criteriaQuery.from(GoodsCenterImportImageInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != batch) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("batch"), batch));
        }

        criteriaQuery.where(restrictions);

        TypedQuery<GoodsCenterImportImageInfo> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }


	@Override
	public GoodsCenterImportImageInfo query(String sn, String batch) {
		if (null == sn) {
			return null;
		}
		try {
			String jpql = "select goodsImportImageInfo from goodsCenterImportImageInfo goodsImportImageInfo where lower(goodsImportImageInfo.sn) =lower(:sn) and goodsImportImageInfo.batch=:batch";
			GoodsCenterImportImageInfo goodsImportImageInfo = entityManager.createQuery(jpql, GoodsCenterImportImageInfo.class).setParameter("sn", sn).setParameter("batch", batch).getSingleResult();
			return goodsImportImageInfo;
		} catch (Exception e) {
			return null;
		}
	}

}

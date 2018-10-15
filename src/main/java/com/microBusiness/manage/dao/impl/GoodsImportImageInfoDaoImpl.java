package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsImportImageInfoDao;
import com.microBusiness.manage.entity.GoodsImportImageInfo;

import org.springframework.stereotype.Repository;

@Repository("goodsImportImageInfoDaoImpl")
public class GoodsImportImageInfoDaoImpl extends BaseDaoImpl<GoodsImportImageInfo, Long> implements GoodsImportImageInfoDao {

    @Override
    public Page<GoodsImportImageInfo> findPage(Pageable pageable, String batch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsImportImageInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsImportImageInfo.class);
        Root<GoodsImportImageInfo> root = criteriaQuery.from(GoodsImportImageInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != batch) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("batch"), batch));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    @Override
    public List<GoodsImportImageInfo> findList(String batch) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsImportImageInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsImportImageInfo.class);
        Root<GoodsImportImageInfo> root = criteriaQuery.from(GoodsImportImageInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != batch) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("batch"), batch));
        }

        criteriaQuery.where(restrictions);

        TypedQuery<GoodsImportImageInfo> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }


	@Override
	public GoodsImportImageInfo query(String sn, String batch) {
		if (null == sn) {
			return null;
		}
		try {
			String jpql = "select goodsImportImageInfo from GoodsImportImageInfo goodsImportImageInfo where lower(goodsImportImageInfo.sn) =lower(:sn) and goodsImportImageInfo.batch=:batch";
			GoodsImportImageInfo goodsImportImageInfo = entityManager.createQuery(jpql, GoodsImportImageInfo.class).setParameter("sn", sn).setParameter("batch", batch).getSingleResult();
			return goodsImportImageInfo;
		} catch (Exception e) {
			return null;
		}
	}

}

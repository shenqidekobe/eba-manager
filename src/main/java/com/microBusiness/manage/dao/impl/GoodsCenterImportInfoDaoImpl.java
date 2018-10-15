package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsCenterImportInfoDao;
import com.microBusiness.manage.entity.GoodsCenterImportInfo;
import com.microBusiness.manage.entity.GoodsCenterImportLog;

import org.springframework.stereotype.Repository;

@Repository
public class GoodsCenterImportInfoDaoImpl extends BaseDaoImpl<GoodsCenterImportInfo, Long> implements GoodsCenterImportInfoDao {

	@Override
    public Page<GoodsCenterImportInfo> findPage(Pageable pageable, GoodsCenterImportLog goodsImportLog) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsCenterImportInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsCenterImportInfo.class);
        Root<GoodsCenterImportInfo> root = criteriaQuery.from(GoodsCenterImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != goodsImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("log"), goodsImportLog));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    @Override
    public List<GoodsCenterImportInfo> findList(GoodsCenterImportLog goodsImportLog , Boolean valid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsCenterImportInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsCenterImportInfo.class);
        Root<GoodsCenterImportInfo> root = criteriaQuery.from(GoodsCenterImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != goodsImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("log"), goodsImportLog));
        }

        if(null != valid){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("valid"), valid));
        }
        criteriaQuery.where(restrictions);

        TypedQuery<GoodsCenterImportInfo> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}

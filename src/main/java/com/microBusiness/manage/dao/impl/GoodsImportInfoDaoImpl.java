package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.GoodsImportInfoDao;
import com.microBusiness.manage.entity.GoodsImportInfo;
import com.microBusiness.manage.entity.GoodsImportLog;

import org.springframework.stereotype.Repository;

@Repository("goodsImportInfoDaoImpl")
public class GoodsImportInfoDaoImpl extends BaseDaoImpl<GoodsImportInfo, Long> implements GoodsImportInfoDao {

    @Override
    public Page<GoodsImportInfo> findPage(Pageable pageable, GoodsImportLog goodsImportLog) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsImportInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsImportInfo.class);
        Root<GoodsImportInfo> root = criteriaQuery.from(GoodsImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != goodsImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("goodsImportLog"), goodsImportLog));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    @Override
    public List<GoodsImportInfo> findList(GoodsImportLog goodsImportLog , Boolean valid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GoodsImportInfo> criteriaQuery = criteriaBuilder.createQuery(GoodsImportInfo.class);
        Root<GoodsImportInfo> root = criteriaQuery.from(GoodsImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != goodsImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("goodsImportLog"), goodsImportLog));
        }

        if(null != valid){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("valid"), valid));
        }
        criteriaQuery.where(restrictions);

        TypedQuery<GoodsImportInfo> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}

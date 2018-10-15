package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedImportInfoDao;
import com.microBusiness.manage.entity.NeedImportInfo;
import com.microBusiness.manage.entity.NeedImportLog;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class NeedImportInfoDaoImpl extends BaseDaoImpl<NeedImportInfo, Long> implements NeedImportInfoDao {

    @Override
    public Page<NeedImportInfo> findPage(Pageable pageable, NeedImportLog needImportLog) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedImportInfo> criteriaQuery = criteriaBuilder.createQuery(NeedImportInfo.class);
        Root<NeedImportInfo> root = criteriaQuery.from(NeedImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != needImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("needImportLog"), needImportLog));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


    @Override
    public List<NeedImportInfo> findList(NeedImportLog needImportLog , Boolean valid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NeedImportInfo> criteriaQuery = criteriaBuilder.createQuery(NeedImportInfo.class);
        Root<NeedImportInfo> root = criteriaQuery.from(NeedImportInfo.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (null != needImportLog) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("needImportLog"), needImportLog));
        }

        if(null != valid){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("valid"), valid));
        }
        criteriaQuery.where(restrictions);

        TypedQuery<NeedImportInfo> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}

package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.VerificationDao;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Verification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("verificationDaoImpl")
public class VerificationDaoImpl extends BaseDaoImpl<Verification, Long> implements VerificationDao {

    @Override
    public List<Verification> findListByBatchNo(String batchNo) {
        String jpql = "select ver from Verification ver where ver.batchNo=:batchNo";
        return entityManager.createQuery(jpql, Verification.class)
                .setParameter("batchNo", batchNo)
                .getResultList();
    }

    @Override
    public Verification findByTag(String tag) {
        String sql = "select ver from Verification ver where ver.tag = :tag";
        Verification ver = entityManager.createQuery(sql, Verification.class).setParameter("tag", tag).getSingleResult();
        return ver;
    }
}

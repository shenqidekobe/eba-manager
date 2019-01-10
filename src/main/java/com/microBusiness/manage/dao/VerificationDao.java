package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.Verification;

import java.util.List;

public interface VerificationDao extends BaseDao<Verification, Long> {

    List<Verification> findListByBatchNo(String batchNo);

    Verification findByTag(String tag);
}

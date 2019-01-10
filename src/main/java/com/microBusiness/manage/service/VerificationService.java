package com.microBusiness.manage.service;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.entity.Verification;

import java.util.List;

public interface VerificationService extends BaseService<Verification, Long> {

    List<Verification> findByBatchNo(String batchNo);

    Verification findByTag(String tag);
}

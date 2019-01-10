package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.Filter;
import com.microBusiness.manage.Order;
import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.CouponCodeDao;
import com.microBusiness.manage.dao.VerificationDao;
import com.microBusiness.manage.entity.Shipping;
import com.microBusiness.manage.entity.Verification;
import com.microBusiness.manage.service.VerificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("verificationServiceImpl")
public class VerificationServiceImpl extends BaseServiceImpl<Verification, Long> implements VerificationService {

    @Resource(name = "verificationDaoImpl")
    private VerificationDao verificationDao;


    @Override
    public List<Verification> findByBatchNo(String batchNo) {
        return verificationDao.findListByBatchNo(batchNo);

    }

    @Override
    public Verification findByTag(String tag) {
        return verificationDao.findByTag(tag);
    }
}

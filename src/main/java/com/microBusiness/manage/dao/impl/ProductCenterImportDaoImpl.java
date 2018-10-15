package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.ProductCenterImportDao;
import com.microBusiness.manage.entity.ProductCenterImport;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/6 上午10:46
 * Describe:
 * Update:
 */
@Repository
public class ProductCenterImportDaoImpl extends BaseDaoImpl<ProductCenterImport, Long> implements ProductCenterImportDao {

    @Override
    public boolean snExists(String sn) {

        if (StringUtils.isEmpty(sn)) {
            return false;
        }

        String jpql = "select count(*) from ProductCenterImport productCenter where lower(productCenter.sn) = lower(:sn)";
        Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
        return count > 0;
    }

}

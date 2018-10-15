package com.microBusiness.manage.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.BindPhoneSmsDao;
import com.microBusiness.manage.entity.BindPhoneSms;

@Repository
public class BindPhoneSmsDaoImpl extends BaseDaoImpl<BindPhoneSms, Long> implements BindPhoneSmsDao {

	@Override
	public BindPhoneSms findBindPhoneSms(BindPhoneSms bindPhoneSms) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BindPhoneSms> criteriaQuery = criteriaBuilder.createQuery(BindPhoneSms.class);
        Root<BindPhoneSms> root = criteriaQuery.from(BindPhoneSms.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != bindPhoneSms.getMobile()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("mobile"), bindPhoneSms.getMobile()));
        }

        if (null != bindPhoneSms.getStatus()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), bindPhoneSms.getStatus()));
        }

        if (null != bindPhoneSms.getAdminId()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("adminId"), bindPhoneSms.getAdminId()));
        }

        if (null != bindPhoneSms.getCode()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("code"), bindPhoneSms.getCode()));
        }

        criteriaQuery.where(restrictions);

        // TODO: 2017/2/8 需要改进
        List<javax.persistence.criteria.Order> orderList = new ArrayList<Order>();
        orderList.add(criteriaBuilder.desc(root.get("sendTime")));
        criteriaQuery.orderBy(orderList);

        TypedQuery<BindPhoneSms> query = entityManager.createQuery(criteriaQuery);

        List<BindPhoneSms> results = query.getResultList();
        if(CollectionUtils.isEmpty(results)){
            return null ;
        }
        return results.get(0) ;
	}

	@Override
	public Long countBindPhoneSms(BindPhoneSms bindPhoneSms, boolean isToday) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BindPhoneSms> criteriaQuery = criteriaBuilder.createQuery(BindPhoneSms.class);
        Root<BindPhoneSms> root = criteriaQuery.from(BindPhoneSms.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != bindPhoneSms.getMobile()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("mobile"), bindPhoneSms.getMobile()));
        }

        if (null != bindPhoneSms.getStatus()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), bindPhoneSms.getStatus()));
        }

        if (null != bindPhoneSms.getAdminId()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("adminId"), bindPhoneSms.getAdminId()));
        }

        if(isToday){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("sendTime").as(Date.class), criteriaBuilder.currentDate()));
        }

        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery , null) ;
	}

}

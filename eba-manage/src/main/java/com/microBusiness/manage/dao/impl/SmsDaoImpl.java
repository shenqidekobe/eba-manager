package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.BaseDao;
import com.microBusiness.manage.entity.Sms;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.jpa.criteria.expression.function.CurrentDateFunction;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/2/7.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class SmsDaoImpl extends BaseDaoImpl<Sms , Long> implements SmsDao {
    @Override
    public Sms findSms(Sms sms) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sms> criteriaQuery = criteriaBuilder.createQuery(Sms.class);
        Root<Sms> root = criteriaQuery.from(Sms.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != sms.getMobile()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("mobile"), sms.getMobile()));
        }

        if (null != sms.getStatus()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), sms.getStatus()));
        }

        if (null != sms.getUserId()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("userId"), sms.getUserId()));
        }

        if (null != sms.getCode()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("code"), sms.getCode()));
        }

        criteriaQuery.where(restrictions);

        // TODO: 2017/2/8 需要改进
        List<javax.persistence.criteria.Order> orderList = new ArrayList<Order>();
        orderList.add(criteriaBuilder.desc(root.get("sendTime")));
        criteriaQuery.orderBy(orderList);

        TypedQuery<Sms> query = entityManager.createQuery(criteriaQuery);

        List<Sms> results = query.getResultList();
        if(CollectionUtils.isEmpty(results)){
            return null ;
        }
        return results.get(0) ;
    }

    @Override
    public Long countSms(Sms sms , boolean isToday) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sms> criteriaQuery = criteriaBuilder.createQuery(Sms.class);
        Root<Sms> root = criteriaQuery.from(Sms.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != sms.getMobile()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("mobile"), sms.getMobile()));
        }

        if (null != sms.getStatus()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), sms.getStatus()));
        }

        if (null != sms.getUserId()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("userId"), sms.getUserId()));
        }

        if(isToday){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.get("sendTime").as(Date.class), criteriaBuilder.currentDate()));
        }

        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery , null) ;
    }
}

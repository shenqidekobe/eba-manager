/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.ass.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodsSyncLogDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.dto.GoodsSyncDto;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoodsSyncLog;
import com.microBusiness.manage.util.DateUtils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

@Repository("assGoodsSyncLogDaoImpl")
public class AssGoodsSyncLogDaoImpl extends BaseDaoImpl<AssGoodsSyncLog, Long> implements AssGoodsSyncLogDao {

	@Override
	public Page<AssGoodsSyncLog> query(String startDate, String endDate, String searchName, Pageable pageable, AssChildMember assChildMember) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<AssGoodsSyncLog> criteriaQuery = criteriaBuilder.createQuery(AssGoodsSyncLog.class);
			Root<AssGoodsSyncLog> root = criteriaQuery.from(AssGoodsSyncLog.class);
			criteriaQuery.select(root);
			Join<AssGoodsSyncLog , AssCustomerRelation> assCustomerRelationJoin = root.join("assCustomerRelation" , JoinType.LEFT);
			Join<AssGoodsSyncLog , AssChildMember> assChildMemberJoin = root.join("beAssChildMember" , JoinType.LEFT);
			Predicate restrictions = criteriaBuilder.conjunction();
			if (startDate != null && !startDate.equals("")) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), DateUtils.specifyDateZero(sdf.parse(startDate))));
			}
			if (endDate != null && !endDate.equals("")) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), DateUtils.specifyDatetWentyour(sdf.parse(endDate))));
			}
			if(searchName != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(assCustomerRelationJoin.<String>get("clientName") , "%"+searchName+"%") , criteriaBuilder.like(assCustomerRelationJoin.<String>get("theme"), "%"+searchName+"%"), criteriaBuilder.like(assChildMemberJoin.<String>get("nickName"), "%"+searchName+"%")));
			}
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
			criteriaQuery.where(restrictions);
			return super.findPage(criteriaQuery, pageable);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public List<GoodsSyncDto> queryAssGoodsSyncLogByDate(Date startDate, Date endDate, AssChildMember assChildMember) {
		StringBuffer buffer = new StringBuffer("select count(1) syncNumber, DATE(agsl.create_date) reportDate from ass_goods_sync_log agsl ");
		buffer.append(" WHERE 1=1  ");
		if (startDate != null) {
			buffer.append(" and agsl.create_date >= :startDate ");
		}
		if (endDate != null) {
			buffer.append(" AND agsl.create_date <= :endDate ");
		}
		if (endDate != null) {
			buffer.append(" AND agsl.ass_child_member = :assChildMember ");
		}
		buffer.append(" GROUP BY DATE(create_date) ");
		Query query = entityManager.createNativeQuery(buffer.toString());
		if (startDate != null) {
			query.setParameter("startDate",startDate);
		}
		if (endDate != null) {
			query.setParameter("endDate",endDate);
		}
		if (endDate != null) {
			query.setParameter("assChildMember",assChildMember);
		}
		query.unwrap(SQLQuery.class).addScalar("syncNumber", IntegerType.INSTANCE).addScalar("reportDate", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(GoodsSyncDto.class));
		List<GoodsSyncDto> lists = query.getResultList();
		return lists;
	}

	@Override
	public Integer queryTotalByDate(Date startDate, Date endDate, AssChildMember assChildMember) {
		Integer number;
		try {
			StringBuffer buffer = new StringBuffer("select SUM(t.c) n from (");
			buffer.append(" SELECT count(1) c FROM ass_goods_sync_log agsl");
			buffer.append(" WHERE 1=1  ");
			if (startDate != null) {
				buffer.append(" and agsl.create_date >= :startDate ");
			}
			if (endDate != null) {
				buffer.append(" AND agsl.create_date <= :endDate ");
			}
			if (endDate != null) {
				buffer.append(" AND agsl.ass_child_member = :assChildMember ");
			}
			buffer.append(" GROUP BY DATE(create_date) ) t");
			Query query = entityManager.createNativeQuery(buffer.toString());
			if (startDate != null) {
				query.setParameter("startDate",startDate);
			}
			if (endDate != null) {
				query.setParameter("endDate",endDate);
			}
			if (endDate != null) {
				query.setParameter("assChildMember",assChildMember);
			}
			number = ((BigDecimal) query.getSingleResult()).intValue();

			return number;
		} catch (Exception e) {
			return 0;
		}

	}

}
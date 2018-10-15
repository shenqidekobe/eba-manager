/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.entity.*;

import com.microBusiness.manage.entity.Member;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

@Repository("memberDaoImpl")
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements MemberDao {

	public boolean usernameExists(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("username", username).getSingleResult();
		return count > 0;
	}

	public boolean emailExists(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	public Member find(String loginPluginId, String openId) {
		if (StringUtils.isEmpty(loginPluginId) || StringUtils.isEmpty(openId)) {
			return null;
		}
		try {
			String jpql = "select members from Member members where members.loginPluginId = :loginPluginId and members.openId = :openId";
			return entityManager.createQuery(jpql, Member.class).setParameter("loginPluginId", loginPluginId).setParameter("openId", openId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Member findByUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.username) = lower(:username)";
			return entityManager.createQuery(jpql, Member.class).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Member> findListByEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return Collections.emptyList();
		}
		String jpql = "select members from Member members where lower(members.email) = lower(:email)";
		return entityManager.createQuery(jpql, Member.class).setParameter("email", email).getResultList();
	}

	public Page<Member> findPage(Member.RankingType rankingType, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		if (rankingType != null) {
			switch (rankingType) {
			case point:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("point")));
				break;
			case balance:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("balance")));
				break;
			case amount:
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get("amount")));
				break;
			}
		}
		return super.findPage(criteriaQuery, pageable);
	}

	public Long registerMemberCount(Date beginDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public void clearAttributeValue(MemberAttribute memberAttribute) {
		if (memberAttribute == null || memberAttribute.getType() == null || memberAttribute.getPropertyIndex() == null) {
			return;
		}

		String propertyName;
		switch (memberAttribute.getType()) {
		case text:
		case select:
		case checkbox:
			propertyName = Member.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
			break;
		default:
			propertyName = String.valueOf(memberAttribute.getType());
			break;
		}
		String jpql = "update Member members set members." + propertyName + " = null";
		entityManager.createQuery(jpql).executeUpdate();
	}


	public Member findByOpenId(String openId) {
		if (StringUtils.isEmpty(openId)) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.openId) = lower(:openId)";
			return entityManager.createQuery(jpql, Member.class).setParameter("openId", openId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Member findByMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.mobile) = lower(:mobile)";
			return entityManager.createQuery(jpql, Member.class).setParameter("mobile", mobile).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 获取最后一次下单时间需要提醒的用户
	 * @param startRow
	 * @param offset
	 * @param compareDate
	 * @return
	 */
	@Override
	public List<Object[]> getMemberToNotice(int startRow, int offset , Date compareDate) {

		StringBuffer findSql = new StringBuffer("select member.* , TIMESTAMPDIFF(DAY,DATE(orders.create_date),DATE(:compareDate)) noOrderDays , supplierSupplier.notice_day noticeDay from xx_member member")
				.append(" left join xx_order orders on orders.member=member.id ")
				.append(" left join t_need need ON member.need = need.id")
				.append(" left join t_supplier_assign_relation assRel ON assRel.need = need.id")
				.append(" left join t_supplier_supplier supplierSupplier ON assRel.supply_relation = supplierSupplier.id")
				.append(" where 1=1")
				.append(" and supplierSupplier.status=").append(SupplierSupplier.Status.inTheSupply.ordinal())
				.append(" and orders.supply_type=").append(SupplyType.formal.ordinal())
				.append(" and need.type=").append(Need.Type.general.ordinal())
				.append(" and supplierSupplier.start_date <= orders.create_date")
				.append(" and orders.create_date <= end_date")
				.append(" and supplierSupplier.open_notice=").append(true)
				.append(" group by orders.member")
				.append(" having noOrderDays>=noticeDay and MOD(noOrderDays , noticeDay)=0");


		Query query = entityManager.createNativeQuery(findSql.toString()) ;

		query.unwrap(SQLQuery.class).addScalar("noOrderDays" , IntegerType.INSTANCE).addEntity(Member.class);

		query.setParameter("compareDate" , compareDate);
		query.setFirstResult(startRow);
		query.setMaxResults(offset);

		List result = query.getResultList();

		return query.getResultList();
	}

}
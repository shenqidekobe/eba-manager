package com.microBusiness.manage.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberIncomeDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.MemberIncome;

@Repository("memberIncomeDaoImpl")
public class MemberIncomeDaoImpl extends BaseDaoImpl<MemberIncome, Long> implements MemberIncomeDao {
	
	@Override
	public MemberIncome getByCorreId(Long correId,String types) {
		if (correId==null) {
			return null;
		}
		try {
			String jpql = "select a from MemberIncome a where a.correId = :correId and a.types=:types";
			List<MemberIncome> list=entityManager.createQuery(jpql, MemberIncome.class).
					setParameter("correId", correId).
					setParameter(types, types).getResultList();
			return list.isEmpty()?null:list.get(0);
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<MemberIncome> query(ChildMember member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberIncome> criteriaQuery = criteriaBuilder.createQuery(MemberIncome.class);
		Root<MemberIncome> root = criteriaQuery.from(MemberIncome.class);
		criteriaQuery.select(root);
		Predicate restrictions=criteriaBuilder.disjunction();

		restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("member"), member));

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
	
	public Page<MemberIncome> findPage(String type, ChildMember member,
			Date startDate,Date endDate,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberIncome> criteriaQuery = criteriaBuilder.createQuery(MemberIncome.class);
		Root<MemberIncome> root = criteriaQuery.from(MemberIncome.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (type != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("types"), type));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}
package com.microBusiness.manage.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.WithdrawDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Withdraw;
import com.microBusiness.manage.entity.Withdraw.Withdraw_Status;

@Repository("withdrawDaoImpl")
public class WithdrawDaoImpl extends BaseDaoImpl<Withdraw, Long> implements WithdrawDao {

	@Override
	public List<Withdraw> query(ChildMember member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Withdraw> criteriaQuery = criteriaBuilder.createQuery(Withdraw.class);
		Root<Withdraw> root = criteriaQuery.from(Withdraw.class);
		criteriaQuery.select(root);
		Predicate restrictions=criteriaBuilder.disjunction();

		restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("member"), member));

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
	
	
	public Page<Withdraw> findPage(Withdraw_Status status, ChildMember member,Date startDate,
			Date endDate,String timeSearch,Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Withdraw> criteriaQuery = criteriaBuilder.createQuery(Withdraw.class);
		Root<Withdraw> root = criteriaQuery.from(Withdraw.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if(StringUtils.isNotEmpty(timeSearch)){
			if(timeSearch.equalsIgnoreCase("createTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
				}
			}else if(timeSearch.equalsIgnoreCase("processTime")) {
				if (startDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("processTime"), startDate));
				}
				if (endDate != null) {
					restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("processTime"), endDate));
				}
			}
		}else {
			if (startDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
			}
			if (endDate != null) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
			}
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

}
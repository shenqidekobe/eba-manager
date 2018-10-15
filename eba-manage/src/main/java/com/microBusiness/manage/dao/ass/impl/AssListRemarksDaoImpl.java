package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssListRemarksDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRemarks;

@Repository("assListRemarksDaoImpl")
public class AssListRemarksDaoImpl extends BaseDaoImpl<AssListRemarks, Long> implements AssListRemarksDao{

	@Override
	public Page<AssListRemarks> getAssListRemarks(Pageable pageable, AssList assList,Long assListRemarksId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssListRemarks> criteriaQuery = criteriaBuilder.createQuery(AssListRemarks.class);
		Root<AssListRemarks> root = criteriaQuery.from(AssListRemarks.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assListRemarksId != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Long>get("id"),assListRemarksId));
		}
		if (assList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assList"), assList));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<AssListRemarks> getRemarksForList(AssList assList) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssListRemarks> criteriaQuery = criteriaBuilder.createQuery(AssListRemarks.class);
		Root<AssListRemarks> root = criteriaQuery.from(AssListRemarks.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assList"), assList));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findList(criteriaQuery, null, null, null, null);
	}

}

package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssListRelationDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.entity.ass.AssListRemarks;

@Repository("assListRelationDaoImpl")
public class AssListRelationDaoImpl extends BaseDaoImpl<AssListRelation, Long> implements AssListRelationDao{

	@Override
	public Page<AssListRelation> getAssListRelations(Pageable pageable, AssList assList, Long assListRelationsId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssListRelation> criteriaQuery = criteriaBuilder.createQuery(AssListRelation.class);
		Root<AssListRelation> root = criteriaQuery.from(AssListRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assListRelationsId != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Long>get("id"),assListRelationsId));
		}
		if (assList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assList"), assList));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public AssListRelation findByChildMember(AssChildMember assChildMember,
			AssList assList) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssListRelation> criteriaQuery = criteriaBuilder.createQuery(AssListRelation.class);
		Root<AssListRelation> root = criteriaQuery.from(AssListRelation.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		if (assList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assList"), assList));
		}
		criteriaQuery.where(restrictions);
		List<AssListRelation> assListRelations=this.findList(criteriaQuery, null, null, null, null);
		if (assListRelations.size()>0) {
			return assListRelations.get(0);
		}
		return null;
	}

}

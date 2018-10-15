package com.microBusiness.manage.dao.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ShareNotesDao;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.ShareNotes;

@Repository
public class ShareNotesDaoImpl extends BaseDaoImpl<ShareNotes, Long> implements ShareNotesDao {

	@Override
	public Page<ShareNotes> findPage(Pageable pageable, Order order,
			Long shareNotesId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ShareNotes> criteriaQuery = criteriaBuilder.createQuery(ShareNotes.class);
		Root<ShareNotes> root = criteriaQuery.from(ShareNotes.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (shareNotesId != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("id"),shareNotesId));
		}
		if (order != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("order"), order));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

}

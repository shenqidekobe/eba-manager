package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssCollectionCardDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCard;
import com.microBusiness.manage.entity.ass.AssCardGoods;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCollectionCard;

@Repository
public class AssCollectionCardDaoImpl extends BaseDaoImpl<AssCollectionCard, Long> implements AssCollectionCardDao {

	@Override
	public Page<AssCollectionCard> findPage(AssChildMember assChildMember, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssCollectionCard> criteriaQuery = criteriaBuilder.createQuery(AssCollectionCard.class);
		Root<AssCollectionCard> root = criteriaQuery.from(AssCollectionCard.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assChildMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public AssCollectionCard getAssCollectionCard(AssChildMember assChildMember, AssCard assCard) {
		String jpql = "select assCollectionCard from AssCollectionCard assCollectionCard where assCollectionCard.assCard =:assCard and assCollectionCard.assChildMember=:assChildMember and assCollectionCard.deleted=0";
		List<AssCollectionCard> list=entityManager.createQuery(jpql, AssCollectionCard.class).setParameter("assCard", assCard).setParameter("assChildMember", assChildMember).getResultList();
		if (list.size() >0) {
			return list.get(0);
		}else {
			return null;
		}
	}

}

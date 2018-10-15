/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Receiver;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.dao.ReceiverDao;
import com.microBusiness.manage.entity.Member;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("receiverDaoImpl")
public class ReceiverDaoImpl extends BaseDaoImpl<Receiver, Long> implements ReceiverDao {

	public Receiver findDefault(Member member) {
		if (member == null) {
			return null;
		}
		try {
			String jpql = "select receiver from Receiver receiver where receiver.member = :member and receiver.isDefault = true";
			return entityManager.createQuery(jpql, Receiver.class).setParameter("member", member).getSingleResult();
		} catch (NoResultException e) {
			try {
				String jpql = "select receiver from Receiver receiver where receiver.member = :member order by receiver.modifyDate desc";
				return entityManager.createQuery(jpql, Receiver.class).setParameter("member", member).setMaxResults(1).getSingleResult();
			} catch (NoResultException e1) {
				return null;
			}
		}
	}

	public Page<Receiver> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Receiver> criteriaQuery = criteriaBuilder.createQuery(Receiver.class);
		Root<Receiver> root = criteriaQuery.from(Receiver.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public void setDefault(Receiver receiver) {
		Assert.notNull(receiver);
		Assert.notNull(receiver.getMember());

		receiver.setIsDefault(true);
		if (receiver.isNew()) {
			String jpql = "update Receiver receiver set receiver.isDefault = false where receiver.member = :member and receiver.isDefault = true";
			entityManager.createQuery(jpql).setParameter("member", receiver.getMember()).executeUpdate();
		} else {
			String jpql = "update Receiver receiver set receiver.isDefault = false where receiver.member = :member and receiver.isDefault = true and receiver != :receiver";
			entityManager.createQuery(jpql).setParameter("member", receiver.getMember()).setParameter("receiver", receiver).executeUpdate();
		}
	}

	@Override
	public List<Receiver> find(Member member, boolean isDefault) {
		try {
			StringBuffer buffer = new StringBuffer("select receiver from Receiver receiver where 1=1");
			if(null != member) {
				buffer.append(" and receiver.member = :member");
			}
			buffer.append(" and receiver.isDefault = :isDefault");
			//buffer.append(" and receiver.supplier is null");
			Query query = entityManager.createQuery(buffer.toString(), Receiver.class);
			if(null != member) {
				query.setParameter("member", member);
			}
			query.setParameter("isDefault", isDefault);
			return query.getResultList();
		} catch (NoResultException e1) {
			return null;
		}
		
	}

	@Override
	public Receiver find(Member member, Supplier supplier) {
		try {
			StringBuffer buffer = new StringBuffer("select receiver from Receiver receiver where 1=1");
			if(null != member) {
				buffer.append(" and receiver.member = :member");
			}
			if(null != supplier) {
				buffer.append(" and receiver.supplier = :supplier");
			}
			Query query = entityManager.createQuery(buffer.toString(), Receiver.class);
			if(null != member) {
				query.setParameter("member", member);
			}
			if(null != supplier) {
				query.setParameter("supplier", supplier);
			}
			List<Receiver> list=query.getResultList();
			if (CollectionUtils.isNotEmpty(list)) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<Receiver> findList(Member member, Supplier supplier) {
		try {
			StringBuffer buffer = new StringBuffer("select * from xx_receiver receiver where 1=1");
			if(null != member) {
				buffer.append(" and receiver.member = :member");
			}
			buffer.append(" and (receiver.type = 0 or (receiver.type = 1");
			if(null != supplier) {
				buffer.append("  and receiver.supplier = :supplier");
			}
			buffer.append("))");
			buffer.append(" and receiver.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString(), Receiver.class);
			if(null != member) {
				query.setParameter("member", member);
			}
			if(null != supplier) {
				query.setParameter("supplier", supplier);
			}
			return query.getResultList();
		} catch (NoResultException e1) {
			return null;
		}
		
	}

}
package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.MemberMemberDao;
import com.microBusiness.manage.entity.InventoryFormLog;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.MemberMember;

@Repository("memberMemberDaoImpl")
public class MemberMemberDaoImpl extends BaseDaoImpl<MemberMember, Long> implements MemberMemberDao {

	@Override
	public Page<MemberMember> findPage(Pageable pageable, Member member,
			Member byMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemberMember> criteriaQuery = criteriaBuilder.createQuery(MemberMember.class);
		Root<MemberMember> root = criteriaQuery.from(MemberMember.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (byMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("byMember"), byMember));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public boolean telExists(Member member, Member byMember) {
		String jpql = "select count(*) from MemberMember memberMember where memberMember.member=:member and memberMember.byMember=:byMember and memberMember.deleted=:deleted";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("member", member)
				.setParameter("byMember", byMember).setParameter("deleted", false).getSingleResult();
		return count > 0;
	}

	@Override
	public MemberMember verifyTelExists(String tel, Member member, MemberMember memberMember) {
		StringBuffer buffer = new StringBuffer("select memberMember.* from t_member_member memberMember");
		buffer.append(" INNER JOIN xx_member member on memberMember.by_member = member.id where 1=1");
		buffer.append(" and memberMember.member = :member and member.mobile = :tel");
		buffer.append(" and memberMember.deleted = 0");
		if(memberMember != null) {
			buffer.append(" and memberMember.id != :memberMember");
		}
		Query query = entityManager.createNativeQuery(buffer.toString(),MemberMember.class);
		query.setParameter("member", member.getId());
		query.setParameter("tel", tel);
		if(memberMember != null) {
			query.setParameter("memberMember", memberMember.getId());
		}
		List<MemberMember> list=query.getResultList();
		if (list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public MemberMember find(Member member, Member byMember) {
		String jpql = "select * from t_member_member memberMember where memberMember.member = :member and memberMember.by_member = :byMember and memberMember.deleted = 0";
		List<MemberMember> list = entityManager.createNativeQuery(jpql, MemberMember.class).setParameter("member", member)
				.setParameter("byMember", byMember).getResultList();
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MemberMember> findList(Member member, Member byMember) {
		StringBuffer buffer = new StringBuffer("select * from t_member_member memberMember where 1=1");
		if(null != member) {
			buffer.append(" and memberMember.member = :member");
		}
		if(null != byMember) {
			buffer.append(" and memberMember.by_member = :byMember");
		}
		buffer.append(" and memberMember.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString(), MemberMember.class);
		if(null != member) {
			query.setParameter("member", member.getId());
		}
		if(null != byMember) {
			query.setParameter("byMember", byMember.getId());
		}
		return query.getResultList();
	}

	@Override
	public MemberMember findByTel(Member member, String tel) {
		try {
			StringBuffer buffer = new StringBuffer("select memberMember.* from t_member_member memberMember");
			buffer.append(" INNER JOIN xx_member member on memberMember.by_member = member.id where 1=1");
			buffer.append(" and memberMember.member = :member and member.username = :tel");
			Query query = entityManager.createNativeQuery(buffer.toString(), MemberMember.class);
			query.setParameter("member", member.getId());
			query.setParameter("tel", tel);
			return (MemberMember) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}

}

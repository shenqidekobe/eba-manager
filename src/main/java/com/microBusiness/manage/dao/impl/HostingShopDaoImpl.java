package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.HostingShopDao;
import com.microBusiness.manage.entity.HostingShop;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Shop;

import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.math.BigInteger;
import java.util.List;

@Repository("hostingShopDaoImpl")
public class HostingShopDaoImpl extends BaseDaoImpl<HostingShop, Long> implements HostingShopDao{

    @Override
    public List<HostingShop> findListByShop(Shop shop) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HostingShop> criteriaQuery = criteriaBuilder.createQuery(HostingShop.class);
        Root<HostingShop> root = criteriaQuery.from(HostingShop.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (shop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"),shop));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

	@Override
	public boolean exist(Member member, Member byMember) {
		StringBuffer buffer = new StringBuffer("select count(1) from t_hosting_shop hostingShop where 1=1");
		buffer.append(" and hostingShop.member = :member and hostingShop.by_member = :byMember");
		buffer.append(" and hostingShop.deleted = 0");
		Query query = entityManager.createNativeQuery(buffer.toString());
		query.setParameter("member", member.getId());
		query.setParameter("byMember", byMember.getId());
		Integer count=((BigInteger) query.getSingleResult()).intValue();
		return count > 0;
	}

	@Override
	public List<HostingShop> findListByShop(Need need) {
		StringBuffer buffer = new StringBuffer("select hostingShop.* from t_need need");
		buffer.append(" INNER JOIN t_need_shop needShop on need.id = needShop.needs");
		buffer.append(" INNER JOIN t_shop shop on needShop.shops = shop.id");
		buffer.append(" INNER JOIN t_hosting_shop hostingShop on shop.id = hostingShop.shop where 1=1");
		buffer.append(" and need.id = :need");
		Query query = entityManager.createNativeQuery(buffer.toString(), HostingShop.class);
		query.setParameter("need", need.getId());
		return query.getResultList();
	}

	@Override
	public List<HostingShop> findListByMember(Member byMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HostingShop> criteriaQuery = criteriaBuilder.createQuery(HostingShop.class);
		Root<HostingShop> root = criteriaQuery.from(HostingShop.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (byMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("byMember"),byMember));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}

	@Override
	public HostingShop findByShopAndByMember(Shop shop, Member byMember) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<HostingShop> criteriaQuery = criteriaBuilder.createQuery(HostingShop.class);
		Root<HostingShop> root = criteriaQuery.from(HostingShop.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"),shop));
		}
		if (byMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("byMember"),byMember));
		}
		criteriaQuery.where(restrictions);
		List<HostingShop> list=super.findList(criteriaQuery, null, null, null, null);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
}

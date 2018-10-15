package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.Query;

import com.microBusiness.manage.dao.ass.AssShippingAddressDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssShippingAddress;
import com.microBusiness.manage.entity.ass.AssShippingAddress.Defaults;

import org.springframework.stereotype.Repository;

@Repository("assShippingAddressDaoImpl")
public class AssShippingAddressDaoImpl extends BaseDaoImpl<AssShippingAddress, Long> implements AssShippingAddressDao {

	@Override
	public List<AssShippingAddress> findByChildMember(AssChildMember assChildMember) {
		try {
			String jpql = "select assShippingAddress from AssShippingAddress assShippingAddress where assShippingAddress.assChildMember = :assChildMember and assShippingAddress.deleted =:deleted";
			List<AssShippingAddress> assShippingAddress = entityManager.createQuery(jpql, AssShippingAddress.class).setParameter("assChildMember", assChildMember).setParameter("deleted", false).getResultList();
			return assShippingAddress;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AssShippingAddress> findBYAddress(
			AssChildMember assChildMember, Defaults defaults) {
		StringBuffer sql = new StringBuffer("select assShippingAddress from AssShippingAddress assShippingAddress where 1=1");
		if(null != assChildMember) {
			sql.append(" and assShippingAddress.assChildMember = :assChildMember");
		}
		if(null != defaults) {
			sql.append(" and assShippingAddress.defaults = :defaults");
		}
		sql.append(" and assShippingAddress.deleted =:deleted");
		Query query = entityManager.createQuery(sql.toString(), AssShippingAddress.class);
		if(null != assChildMember) {
			query.setParameter("assChildMember", assChildMember);
		}
		if(null != defaults) {
			query.setParameter("defaults", defaults);
		}
		query.setParameter("deleted", false);
		return query.getResultList();
	}
	
}

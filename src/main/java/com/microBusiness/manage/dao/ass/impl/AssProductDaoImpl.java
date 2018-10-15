package com.microBusiness.manage.dao.ass.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.ass.AssProductDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.entity.ass.AssGoods;
import com.microBusiness.manage.entity.ass.AssProduct;
@Repository("assProductDaoImpl")
public class AssProductDaoImpl extends BaseDaoImpl<AssProduct, Long> implements AssProductDao {

	@Override
	public boolean snExists(String sn) {
		if (StringUtils.isEmpty(sn)) {
			return false;
		}

		String jpql = "select count(*) from AssProduct assProduct where lower(assProduct.sn) = lower(:sn)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("sn", sn).getSingleResult();
		return count > 0;
	}

	@Override
	public List<AssProduct> findBySpecifications(Long id) {
		if(null == id) {
			return null;
		}
		StringBuffer findSql = new StringBuffer("select assProduct.* from ass_product assProduct");
		findSql.append(" LEFT JOIN ass_goods assGood on assProduct.ass_goods = assGood.id");
		findSql.append(" where 1=1 and assGood.deleted = 0 and assProduct.ass_goods=:id and assProduct.deleted = 0 order by create_date asc");
		return entityManager.createNativeQuery(findSql.toString(), AssProduct.class).setParameter("id", id).getResultList();
	}

	@Override
	public List<AssProduct> findByList(AssGoods assGoods) {
		if(assGoods.getId() == null) {
			return null;
		}
		String jpql = "select assProduct from AssProduct assProduct where assProduct.assGoods=:assGood and assProduct.deleted=0";
		List<AssProduct> assProducts = entityManager.createQuery(jpql, AssProduct.class).setParameter("assGood", assGoods).getResultList();
		return assProducts;
	}

}

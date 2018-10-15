package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.microBusiness.manage.dao.FavorCompanyDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("favorCompanyDaoImpl")
public class FavorCompanyDaoImpl extends BaseDaoImpl<FavorCompany, Long> implements FavorCompanyDao {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public FavorCompany getFavorCompanyIsNull(Long adminId, Long supplierId) {
		if (adminId == null || supplierId == null) {
			return null;
		}
		try{
			String jpql = "select favorCompany from FavorCompany favorCompany where favorCompany.adminId = :adminId and favorCompany.supplierId = :supplierId and delflag = 0";
			return entityManager.createQuery(jpql, FavorCompany.class).setParameter("adminId", adminId).setParameter("supplierId", supplierId).getSingleResult();
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getFavorCompanyIsNull error : " , e);
			return null ;
		}
	}

	@Override
	public boolean updateDelflag(Long supplierId, Long adminId , FavorCompany.Delflag delflag) {
		try {
			//String jpql = "update FavorCompany favorCompany set favorCompany.delflag=:delflag where favorCompany.adminId=:adminId and favorCompany.supplierId=:supplierId";
			String jpql = "update t_favor_company favorCompany set favorCompany.delflag=:delflag where favorCompany.admin_id=:adminId and favorCompany.supplier_id=:supplierId";
			Query query = entityManager.createNativeQuery(jpql.toString()).setParameter("delflag", delflag.ordinal())
			.setParameter("adminId", adminId).setParameter("supplierId", supplierId);
			int i = query.executeUpdate();
			return query.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<FavorCompany> getFavorCompany(Supplier supplier) {
		StringBuffer findSql = new StringBuffer("select t_favor_company.* from t_favor_company");
		findSql.append(" left join xx_admin on xx_admin.id = t_favor_company.admin_id");
		findSql.append(" left join t_supplier on t_supplier.id = xx_admin.supplier");
		findSql.append(" where t_supplier.id=:supplierId");
		findSql.append(" and t_favor_company.delflag=0");
		
		Query query = entityManager.createNativeQuery(findSql.toString(), FavorCompany.class);
		query.setParameter("supplierId", supplier.getId());
		
		return query.getResultList();
	}

}

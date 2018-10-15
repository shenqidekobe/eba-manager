package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.FavorCompanyGoodsDao;
import com.microBusiness.manage.dto.CompanyGoodsSupplierDto;
import com.microBusiness.manage.dto.GoodNeedDto;
import com.microBusiness.manage.entity.CompanyGoods;
import com.microBusiness.manage.entity.CompanyGoods.Delflag;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.FavorCompanyGoods;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("favorCompanyGoodsDaoImpl")
public class FavorCompanyGoodsDaoImpl extends BaseDaoImpl<FavorCompanyGoods, Long> implements FavorCompanyGoodsDao {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public FavorCompanyGoods getFavorCompanyGoodsIsNull(Long adminId, Long goodsId) {
		if (adminId == null || goodsId == null) {
			return null;
		}
		try{
			String jpql = "select favorCompanyGoods from FavorCompanyGoods favorCompanyGoods where favorCompanyGoods.adminId = :adminId and favorCompanyGoods.companyGoodsId = :companyGoodsId and delflag = 0";
			return entityManager.createQuery(jpql, FavorCompanyGoods.class).setParameter("adminId", adminId).setParameter("companyGoodsId", goodsId).getSingleResult();
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getFavorCompanyIsNull error : " , e);
			return null ;
		}
	}

	@Override
	public Page<CompanyGoodsSupplierDto> findPage(Pageable pageable,
			Long adminId, Delflag delflag) {
		StringBuffer findSql = new StringBuffer("select supplier.name supplierName,companyGoods.* from t_supplier supplier");
		findSql.append(" INNER JOIN t_company_goods companyGoods on supplier.id = companyGoods.supplier");
		findSql.append(" INNER JOIN t_favor_company_goods favorCompanyGoods on companyGoods.id = favorCompanyGoods.company_goods_id");
		findSql.append(" where favorCompanyGoods.admin_id=:adminId");
		findSql.append(" and favorCompanyGoods.delflag=:delflag");
		StringBuffer countSql = new StringBuffer("select COUNT(*) from t_supplier supplier");
		countSql.append(" INNER JOIN t_company_goods companyGoods on supplier.id = companyGoods.supplier");
		countSql.append(" INNER JOIN t_favor_company_goods favorCompanyGoods on companyGoods.id = favorCompanyGoods.company_goods_id");
		countSql.append(" where favorCompanyGoods.admin_id=:adminId");
		countSql.append(" and favorCompanyGoods.delflag=:delflag");
		Query query = entityManager.createNativeQuery(findSql.toString());
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		query.setParameter("adminId", adminId);
		query.setParameter("delflag", delflag.ordinal());
		countQuery.setParameter("adminId", adminId);
		countQuery.setParameter("delflag", delflag.ordinal());
		
		query.unwrap(SQLQuery.class).addEntity(CompanyGoods.class).addScalar("supplierName" , StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(CompanyGoodsSupplierDto.class));
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		List<Supplier> result = query.getResultList();
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		return new Page<CompanyGoodsSupplierDto>(query.getResultList(), total, pageable);
	}

	@Override
	public void updateDelflag(Long adminId, Long companyGoodsId, Delflag delflag) {
		try {
			String jpql = "update t_favor_company_goods favorCompanyGoods set favorCompanyGoods.delflag=:delflag where favorCompanyGoods.admin_id=:adminId and favorCompanyGoods.company_goods_id=:companyGoodsId";
			Query query = entityManager.createNativeQuery(jpql.toString()).setParameter("delflag", delflag.ordinal())
			.setParameter("adminId", adminId).setParameter("companyGoodsId", companyGoodsId);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<FavorCompanyGoods> getFavorCompanyGoods(Supplier supplier) {
		StringBuffer findSql = new StringBuffer("select t_favor_company_goods.* from t_favor_company_goods");
		findSql.append(" left join xx_admin on xx_admin.id = t_favor_company_goods.admin_id");
		findSql.append(" left join t_supplier on t_supplier.id = xx_admin.supplier");
		findSql.append(" where t_supplier.id=:supplierId");
		findSql.append(" and t_favor_company_goods.delflag=0");
		
		Query query = entityManager.createNativeQuery(findSql.toString(), FavorCompanyGoods.class);
		query.setParameter("supplierId", supplier.getId());
		
		return query.getResultList();
	}

}

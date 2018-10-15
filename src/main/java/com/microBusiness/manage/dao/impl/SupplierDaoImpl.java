package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dto.SupplierDto;
import com.microBusiness.manage.entity.FavorCompany;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCenter;
import com.microBusiness.manage.entity.SpecificationValue;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Supplier.Status;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Types;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class SupplierDaoImpl extends BaseDaoImpl<Supplier, Long> implements SupplierDao {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean exists(String name) {
		String sql="select count(*) from Supplier supplier where supplier.name = :name and supplier.status=:status";
		Long count = entityManager.createQuery(sql, Long.class).setParameter("name", name).setParameter("status", Supplier.Status.verified).getSingleResult();
		return count>0;
	}
	
	public Page<Supplier> findPage(Pageable pageable , Supplier.Status status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<Supplier> findByStatus(Status... statuss) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);
		criteriaQuery.select(root);
		Predicate restrictions=criteriaBuilder.disjunction();
		if (statuss != null && statuss.length > 0) {
			for (Status status : statuss) {
				restrictions = criteriaBuilder.or(restrictions,criteriaBuilder.equal(root.get("status"), status));
			}
		}
		
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}


	@Override
	public List<Supplier> findFormal(Need need, Pageable pageable) {
		/*select supplier.* from t_supplier supplier
		inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.`supplier`
		inner join t_supplier_assign_relation assRel on supplierRel.id=assRel.`supply_relation`
		where assRel.`need`=5 and ( supplierRel.`start_date`<= now() <= supplierRel.`end_date`  )*/

		StringBuffer sqlSb = new StringBuffer("select supplier.* from t_supplier supplier");
		sqlSb.append(" inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.supplier").append(" inner join t_supplier_assign_relation assRel on supplierRel.id=assRel.supply_relation");

		//StringBuffer countSqlSb = new StringBuffer("select count(supplier.id) from t_supplier supplier");
		//countSqlSb.append(" inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.supplier").append(" inner join t_supplier_assign_relation assRel on supplierRel.id=assRel.supply_relation");

		sqlSb.append(" where 1=1");
		//countSqlSb.append(" where 1=1");
		if(null != need){
			sqlSb.append(" and assRel.need=:needId");
			//countSqlSb.append(" and assRel.need=:needId");
		}

		sqlSb.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());
		sqlSb.append(" and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");

		//countSqlSb.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());
		//countSqlSb.append(" and (supplierRel.start_date <= now() <= supplierRel.end_date)");

		Query query = entityManager.createNativeQuery(sqlSb.toString(), Supplier.class) ;
		//Query countQuery = entityManager.createNativeQuery(countSqlSb.toString()) ;

		if(null != need){
			query.setParameter("needId" , need.getId()) ;
			//countQuery.setParameter("needId" , need.getId()) ;
		}
		List<Supplier> result = query.getResultList();
		//Object totalCount = countQuery.getSingleResult() ;

		return result;
	}

	/**
	 * 通过被供应企业查询供应的企业
	 *
	 * @param bySupplierId
	 * @return
	 */
	@Override
	public List<Supplier> getSupplierFromBy(Long bySupplierId) {
		//select supplier.* from t_supplier supplier
		//inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.by_supplier
		//where  (supplierRel.start_date <= now() and now() <= supplierRel.end_date)

		StringBuffer sqlSb = new StringBuffer("select supplier.* from t_supplier supplier");
		sqlSb.append(" inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.supplier");
		sqlSb.append(" where 1=1");

		if(null != bySupplierId){
			sqlSb.append(" and supplierRel.by_supplier=:bySupplierId");
		}

		sqlSb.append(" and supplierRel.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());
		sqlSb.append(" and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");


		Query query = entityManager.createNativeQuery(sqlSb.toString(), Supplier.class) ;

		if(null != bySupplierId){
			query.setParameter("bySupplierId" , bySupplierId) ;
		}
		List<Supplier> result = query.getResultList();
		return result;
	}

	@Override
	public boolean existaName(String name, Long id) {
		String sql="select count(*) from Supplier supplier where supplier.name = :name and supplier.id !=:id";
		Long count = entityManager.createQuery(sql, Long.class).setParameter("name", name).setParameter("id", id).getSingleResult();
		return count > 0;
	}

	/**
	 * 邀请码是否存在。
	 * @param inviteCode
	 * @return
	 */
	@Override
	public boolean inviteCodeExists(String inviteCode) {
		if (StringUtils.isEmpty(inviteCode)) {
			return false;
		}
		String jpql = "select count(*) from Supplier supplier where lower(supplier.inviteCode) = lower(:inviteCode)";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("inviteCode", inviteCode).getSingleResult();
		return count > 0;
	}

	@Override
	public Supplier getSupplierByInviteCode(String inviteCode) {
		if (StringUtils.isEmpty(inviteCode)) {
			return null;
		}
		try{
			String jpql = "select supplier from Supplier supplier where lower(supplier.inviteCode) = lower(:inviteCode)";
			Supplier supplier = entityManager.createQuery(jpql, Supplier.class).setParameter("inviteCode", inviteCode).getSingleResult();
			return supplier;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getSupplierByInviteCode error : " , e);
			return null ;
		}

	}

	@Override
	public Supplier getSupplierByNeed(Long needId) {
		if (null == needId) {
			return null;
		}
		try{
			String jpql = "select supplier from Need need join need.supplier supplier where lower(need.id) = lower(:needId)";
			Supplier supplier = entityManager.createQuery(jpql, Supplier.class).setParameter("needId", needId).getSingleResult();
			return supplier;
		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("getSupplierByInviteCode error : " , e);
			return null ;
		}
	}

	@Override
	public List<Supplier> getSupplierList(Long count) {
		StringBuffer findSql = new StringBuffer("select supplier.* from t_supplier supplier where supplier.recommend_flag = 1 order by supplier.create_date desc limit :limit ");
		Query query = entityManager.createNativeQuery(findSql.toString() , Supplier.class);
		query.setParameter("limit" , count);
		List<Supplier> lists = query.getResultList();
		return lists;
	}

	@Override
	public Page<Supplier> findPage(Pageable pageable, String name, Boolean status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (status != null) {
			if (status) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), Status.verified));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root.get("status"), Status.verified));
			}
			
		}
		if (name != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("name"), "%"+name+"%"));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<Supplier> findPage(Long adminId, FavorCompany.Delflag delflag, Pageable pageable) {
		StringBuffer findSql = new StringBuffer("select supplier.* from t_supplier supplier");
		findSql.append(" INNER JOIN t_favor_company favorCompany on supplier.id = favorCompany.supplier_id");
		//findSql.append(" INNER JOIN xx_admin admin on favorCompany.admin_id = admin.id");
		findSql.append(" where 1=1 and favorCompany.admin_id=:adminId");
		findSql.append(" and favorCompany.delflag=:delflag");
		
		StringBuffer countSql = new StringBuffer("select count(*) from t_supplier supplier");
		countSql.append(" INNER JOIN t_favor_company favorCompany on supplier.id = favorCompany.supplier_id");
		//countSql.append(" INNER JOIN xx_admin admin on favorCompany.admin_id = admin.id");
		countSql.append(" where 1=1 and favorCompany.admin_id=:adminId");
		countSql.append(" and favorCompany.delflag=:delflag");
		
		Query query = entityManager.createNativeQuery(findSql.toString(), Supplier.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		query.setParameter("adminId", adminId);
		query.setParameter("delflag", delflag.ordinal());
		countQuery.setParameter("adminId", adminId);
		countQuery.setParameter("delflag", delflag.ordinal());
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		List<Supplier> result = query.getResultList();
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		return new Page<Supplier>(query.getResultList(), total, pageable);
	}

	@Override
	public List<Supplier> findBySupplierNeed(Need need) {
		StringBuffer sqlSb = new StringBuffer("select supplier.*  from t_supplier supplier");
		sqlSb.append(" inner join t_supply_need supplyNeed on supplier.id = supplyNeed.supplier")
			 .append(" where 1=1");
		if(null != need){
			sqlSb.append(" and supplyNeed.need=:needId");
		}

		sqlSb.append(" and supplyNeed.status=").append(SupplierSupplier.Status.inTheSupply.ordinal());
		sqlSb.append(" and (supplyNeed.start_date <= now() and now() <= supplyNeed.end_date)");


		Query query = entityManager.createNativeQuery(sqlSb.toString(), Supplier.class) ;

		if(null != need){
			query.setParameter("needId" , need.getId()) ;
		}
		List<Supplier> result = query.getResultList();

		return result;
	}
	
	public List<SupplierDto> findSupplierListByMember(Member member , Long supplierId) {
		StringBuffer sql = new StringBuffer("select distinct supplier.*,supplier.name supplierName,supplyNeed.supplier supplierId,supplyNeed.id relationId  from t_supplier supplier");
		sql.append(" inner join t_supply_need supplyNeed on supplier.id = supplyNeed.supplier");
		sql.append(" inner join t_need need on need.id = supplyNeed.need and supplier.id = supplyNeed.supplier");
		sql.append(" where 1=1");
		sql.append(" and need.member =:memberId");
		sql.append(" and need.need_status = 0");
		sql.append(" and supplyNeed.status=:status");
		sql.append(" and (supplyNeed.start_date <= now() and now() <= supplyNeed.end_date)");
//		if(null != supplierId){
//			sql.append(" and supplier.id=:supplierId");
//		}

		Query query = entityManager.createNativeQuery(sql.toString()) ;
		query.unwrap(SQLQuery.class).addEntity(Supplier.class).addScalar("supplierName", StringType.INSTANCE).addScalar("supplierId", LongType.INSTANCE).addScalar("relationId", LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(SupplierDto.class));
		query.setParameter("memberId" , member.getId()) ;
		query.setParameter("status" , SupplyNeed.Status.SUPPLY.ordinal()) ;
//		if(null != supplierId){
//			query.setParameter("supplierId" , supplierId) ;
//		}

		List<SupplierDto> result = query.getResultList();
		return result;
	}
	
	@Override
	public List<SupplierDto> findFormal(Member member , Long supplierId) {
		StringBuffer sql = new StringBuffer("select supplier.*,sellSupplier.name supplierName,sellSupplier.id supplierId,supplierRel.id relationId from t_supplier supplier");
		sql.append(" inner join t_supplier_supplier supplierRel on supplier.id=supplierRel.supplier");
		sql.append(" inner join t_supplier sellSupplier on sellSupplier.id=supplierRel.by_supplier");
		sql.append(" inner join t_supplier_assign_relation assRel on supplierRel.id=assRel.supply_relation");
		sql.append(" INNER JOIN t_need need on need.id = assRel.need");
		sql.append(" where 1=1");
		sql.append(" and need.need_status = 0");
		if(null != member) {
			sql.append(" and need.member=:memberId");
		}

//		if(null != supplierId){
//			sql.append(" and supplier.id=:supplierId");
//		}


		sql.append(" and supplierRel.status=:status");
		sql.append(" and (supplierRel.start_date <= now() and now() <= supplierRel.end_date)");
		Query query = entityManager.createNativeQuery(sql.toString()) ;
		query.unwrap(SQLQuery.class).addEntity(Supplier.class).addScalar("supplierName", StringType.INSTANCE).addScalar("supplierId", LongType.INSTANCE).addScalar("relationId", LongType.INSTANCE).setResultTransformer(Transformers.aliasToBean(SupplierDto.class));
		
		if(null != member){
			query.setParameter("memberId" , member.getId()) ;
		}

//		if(null != supplierId){
//			query.setParameter("supplierId" , supplierId) ;
//		}

		query.setParameter("status" , SupplierSupplier.Status.inTheSupply.ordinal());
		List<SupplierDto> result = query.getResultList();
		return result;
	}

	@Override
	public Supplier findByAppId(String appId) {
		if (null == appId) {
			return null;
		}
		try{
			String jpql = "select supplier from Supplier supplier where lower(supplier.appId) = lower(:appId)";
			Supplier supplier = entityManager.createQuery(jpql, Supplier.class).setParameter("appId", appId).getSingleResult();
			return supplier;
		}catch (Exception e){
			e.printStackTrace();
			return null ;
		}
	}

	@Override
	public boolean doesItExistName(String name, Types types, Member member) {
		String sql="select count(*) from Supplier supplier where supplier.name = :name and supplier.types = :types and supplier.member=:member and supplier.deleted=0";
		Long count = entityManager.createQuery(sql, Long.class).setParameter("name", name).setParameter("types", types).setParameter("member", member).getSingleResult();
		return count > 0;
	}

	@Override
	public List<Supplier> getSupplierListByMember(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Supplier> criteriaQuery = criteriaBuilder.createQuery(Supplier.class);
		Root<Supplier> root = criteriaQuery.from(Supplier.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery,null,null,null,null);
	}

	@Override
	public List<Supplier> getSupplierListByMember(Member member, ProductCenter productCenter, String name) {
		try {
			StringBuffer buffer = new StringBuffer();
			List<SpecificationValue> specificationValues = productCenter.getSpecificationValues();
			String barCode = productCenter.getBarCode();
			if(StringUtils.isNotEmpty(barCode)) {
				buffer.append("select DISTINCT sup.* from t_supplier sup where sup.id not in (");
				buffer.append(" select DISTINCT supplier.id from t_supplier supplier");
				buffer.append(" LEFT JOIN xx_goods goods on goods.supplier = supplier.id");
				buffer.append(" LEFT JOIN xx_product product on product.goods = goods.id where 1=1");
				if(member != null) {
					buffer.append(" and supplier.member = :member ");
				}
				if(StringUtils.isNotEmpty(barCode)) {
					buffer.append(" and product.bar_code = :barCode");
				}
				buffer.append(" and supplier.deleted = 0 and goods.deleted = 0)");
				if(member != null) {
					buffer.append(" and sup.member = :member");
				}
				buffer.append(" and sup.deleted = 0");
			}else {
				buffer.append("select * from t_supplier sup where sup.id not in(");
				buffer.append("select DISTINCT supplier.id from t_supplier supplier");
				buffer.append(" LEFT JOIN xx_goods goods on goods.supplier = supplier.id");
				buffer.append(" LEFT JOIN xx_product product on product.goods = goods.id where 1=1");
				if(member != null) {
					buffer.append(" and supplier.member = :member ");
				}
				if(name != null) {
					buffer.append(" and goods.name = :name");
				}
				
				if(CollectionUtils.isNotEmpty(specificationValues) && specificationValues.size() > 0) {
					buffer.append(" and product.specification_values = :specificationValues");
				}
				buffer.append(" and supplier.deleted = 0)");
				buffer.append(" and sup.member = :member");
				buffer.append(" and sup.deleted = 0");
			}
			Query query = entityManager.createNativeQuery(buffer.toString(), Supplier.class);
			if(member != null) {
				query.setParameter("member", member.getId());
			}
			if(StringUtils.isNotEmpty(productCenter.getBarCode())) {
				query.setParameter("barCode", barCode);
			}
			if(StringUtils.isEmpty(barCode) && StringUtils.isNotEmpty(name)) {
				query.setParameter("name", name);
			}
			if(StringUtils.isEmpty(barCode) && CollectionUtils.isNotEmpty(specificationValues) && specificationValues.size() > 0) {
				query.setParameter("specificationValues", specificationValues);
			}
			return query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

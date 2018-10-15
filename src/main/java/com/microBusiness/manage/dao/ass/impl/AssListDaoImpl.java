package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssListDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.dto.AssListAndCustomerRelationDto;
import com.microBusiness.manage.dto.AssListStatisticsDto;
import com.microBusiness.manage.dto.AssPurchaseListStatisticsDto;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssCustomerRelation.ShareType;
import com.microBusiness.manage.entity.ass.AssList;
import com.microBusiness.manage.entity.ass.AssList.Status;
import com.microBusiness.manage.entity.ass.AssListRelation;
import com.microBusiness.manage.util.DateUtils;

@Repository("assListDaoImpl")
public class AssListDaoImpl extends BaseDaoImpl<AssList, Long> implements AssListDao{

	/**
	 * 后台获取清单列表
	 * @param status
	 * @param pageable
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param searchName
	 * @return
	 */
	@Override
	public Page<AssList> findPage(Status status, Pageable pageable,
			Supplier supplier, Date startDate, Date endDate, String searchName) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssList> criteriaQuery = criteriaBuilder.createQuery(AssList.class);
		Root<AssList> root = criteriaQuery.from(AssList.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(StringUtils.isNotEmpty(searchName)){
            Join<AssList , Need> needJoin = root.join("need" , JoinType.LEFT);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn") , "%"+searchName+"%") , criteriaBuilder.like(needJoin.<String>get("name"), "%"+searchName+"%")));
        }
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/**
	 * 小程序端获取清单列表
	 * @param pageable
	 * @param assChildMember
	 * @param member
	 * @return
	 */
	@Override
	public Page<AssList> findPage(Pageable pageable,String searchValue,
			AssChildMember assChildMember, Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssList> criteriaQuery = criteriaBuilder.createQuery(AssList.class);
		Root<AssList> root = criteriaQuery.from(AssList.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		Predicate restrictions2 = criteriaBuilder.disjunction();
		
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (assChildMember != null) {
			restrictions2 =criteriaBuilder.or(restrictions2,criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
			
			Subquery<AssListRelation> assListRelationSubquery = criteriaQuery.subquery(AssListRelation.class);
			Root<AssListRelation> assListRelationRoot = assListRelationSubquery.from(AssListRelation.class);
			assListRelationSubquery.select(assListRelationRoot);
			assListRelationSubquery.where(criteriaBuilder.equal(assListRelationRoot.get("assList"), root),criteriaBuilder.equal(assListRelationRoot.get("assChildMember"), assChildMember));
			
			restrictions2 = criteriaBuilder.or(restrictions2, criteriaBuilder.exists(assListRelationSubquery));
			
		}
		if (member != null) {
			restrictions2 =criteriaBuilder.or(restrictions2,criteriaBuilder.equal(root.get("member"), member));
		}
		restrictions= criteriaBuilder.and(restrictions, restrictions2);
		if (StringUtils.isNotBlank(searchValue)) {
			restrictions=criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn"), "%"+searchValue+"%"),criteriaBuilder.like(root.<String>get("clientName"), "%"+searchValue+"%")));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public boolean assListItem(List<Long> ids) {
		if (!CollectionUtils.isNotEmpty(ids)) {
			return false;
		}
		StringBuffer findSql = new StringBuffer("select count(*) from ass_list_item listItem LEFT JOIN ass_list list on listItem.ass_list = list.id");
		findSql.append(" where list.deleted = 0 and listItem.ass_product in(:ids)");
		Query query = entityManager.createNativeQuery(findSql.toString());
		query.setParameter("ids", ids);
		Integer count=((BigInteger) query.getSingleResult()).intValue();
		return count > 0;
	}

	@Override
	public boolean hasSn(String sn) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssList> criteriaQuery = criteriaBuilder.createQuery(AssList.class);
		Root<AssList> root = criteriaQuery.from(AssList.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (sn != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("sn"), sn));
		}
		criteriaQuery.where(restrictions);
		List<AssList> assLists=this.findList(criteriaQuery, null, null, null, null);
		if (assLists.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Long pressMonthCountPurchaseList(AssChildMember assChildMember,
			Date startDate, Date endDate) {
		if (null == assChildMember.getId() || null == startDate || null == endDate) {
			return null;
		}
		String jpql = "select count(*) from AssList assList where assList.assChildMember=:assChildMember and assList.createDate >=:startDate and assList.createDate<=:endDate and assList.deleted=0";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("assChildMember", assChildMember).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getSingleResult();
		return count;
	}

	@Override
	public Page<AssList> findPage(Pageable pageable, String searchValue,
			AssChildMember assChildMember, Date startDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssList> criteriaQuery = criteriaBuilder.createQuery(AssList.class);
		Root<AssList> root = criteriaQuery.from(AssList.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		if(StringUtils.isNotEmpty(searchValue)){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn") , "%"+searchValue+"%") , criteriaBuilder.like(root.<String>get("clientName"), "%"+searchValue+"%")));
        }
		if(null != assChildMember) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("assChildMember"), assChildMember));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public List<AssListStatisticsDto> findByList(AssChildMember assChildMember) {
		if (null == assChildMember.getId() || null == assChildMember) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select year(assList.create_date) years,month(assList.create_date) months ,count(*) counts from ass_list assList where 1=1");
		if(null != assChildMember) {
			sql.append(" and assList.ass_child_member=:assChildMember");
		}
		sql.append(" and assList.deleted = 0");
		sql.append(" GROUP BY years,months ORDER BY years,months asc");
		Query query = entityManager.createNativeQuery(sql.toString()) ;
		if(null != assChildMember) {
			query.setParameter("assChildMember", assChildMember);
		}
		query.unwrap(SQLQuery.class).addScalar("years",IntegerType.INSTANCE).addScalar("months",IntegerType.INSTANCE).addScalar("counts", IntegerType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(AssListStatisticsDto.class));
		List<AssListStatisticsDto> result = query.getResultList();
		return result;
	}

	/**
	 * 采购清单统计(商品看板)
	 */
	@Override
	public List<AssPurchaseListStatisticsDto> assListStatistics(Date startDate,
			Date endDate, AssChildMember assChildMember, ShareType shareType) {
		StringBuffer buffer = new StringBuffer("select SUM(ass.con) number,DATE_FORMAT(ass.createDate,'%m-%d') createDate from(select COUNT(DISTINCT assList.id) con,");
		buffer.append(" date(assList.create_date) createDate from ass_list assList");
		buffer.append(" LEFT JOIN ass_list_item assListItem on assList.id = assListItem.ass_list");
		buffer.append(" LEFT JOIN ass_product assProduct on assListItem.ass_product = assProduct.id");
		buffer.append(" LEFT JOIN ass_goods assGoods on assProduct.ass_goods = assGoods.id");
		buffer.append(" LEFT JOIN ass_goods assGoodsSource on assGoods.source = assGoodsSource.id");
		buffer.append(" LEFT JOIN ass_customer_relation customerRelation on assGoodsSource.ass_customer_relation = customerRelation.id");
		buffer.append(" LEFT JOIN ass_customer_relation relation on assList.ass_customer_relation = relation.id where 1=1");
		
		/*if(null != shareType) {
			buffer.append(" and customerRelation.share_type=:shareType");
		}*/
		if(null != assChildMember && null != shareType) {
			buffer.append(" and ((customerRelation.ass_child_member=:assChildMember and customerRelation.share_type=:shareType) or (relation.ass_child_member=:assChildMember and assList.ass_child_member!=:assChildMember))");
		}
		if(null != startDate) {
			buffer.append(" and assList.create_date >= :startDate");
		}
		if(null != endDate) {
			buffer.append(" and assList.create_date <= :endDate");
		}
		buffer.append(" and assList.deleted = 0");
		buffer.append(" GROUP BY createDate) ass where 1=1 GROUP BY ass.createDate");
		
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(null != shareType) {
			query.setParameter("shareType",shareType.ordinal());
		}
		if(null != assChildMember) {
			query.setParameter("assChildMember", assChildMember);
		}
		if (null != startDate) {
			query.setParameter("startDate",startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate",endDate);
		}
		query.unwrap(SQLQuery.class).addScalar("number", IntegerType.INSTANCE).addScalar("createDate", StringType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(AssPurchaseListStatisticsDto.class));
		return query.getResultList();
	}

	/**
	 *  采购清单统计详情(商品看板)
	 */
	@Override
	public Page<AssListAndCustomerRelationDto> assListStatisticsDetails(
			AssChildMember assChildMember, String startDate, String endDate,
			String searchValue, ShareType shareType, Pageable pageable) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			StringBuffer buffer = new StringBuffer("select DISTINCT assList.*,customerRelation.client_name pClientName,customerRelation.theme pTheme from ass_list assList");
			buffer.append(" LEFT JOIN ass_list_item assListItem on assList.id = assListItem.ass_list");
			buffer.append(" LEFT JOIN ass_product assProduct on assListItem.ass_product = assProduct.id");
			buffer.append(" LEFT JOIN ass_goods assGoods on assProduct.ass_goods = assGoods.id");
			buffer.append(" LEFT JOIN ass_goods assGoodsSource on assGoods.source = assGoodsSource.id");
			buffer.append(" LEFT JOIN ass_customer_relation customerRelation on assGoodsSource.ass_customer_relation = customerRelation.id");
			buffer.append(" LEFT JOIN ass_customer_relation relation on assList.ass_customer_relation = relation.id where 1=1");
			
			StringBuffer countSql = new StringBuffer("select count(DISTINCT assList.id) from ass_list assList");
			countSql.append(" LEFT JOIN ass_list_item assListItem on assList.id = assListItem.ass_list");
			countSql.append(" LEFT JOIN ass_product assProduct on assListItem.ass_product = assProduct.id");
			countSql.append(" LEFT JOIN ass_goods assGoods on assProduct.ass_goods = assGoods.id");
			countSql.append(" LEFT JOIN ass_goods assGoodsSource on assGoods.source = assGoodsSource.id");
			countSql.append(" LEFT JOIN ass_customer_relation customerRelation on assGoodsSource.ass_customer_relation = customerRelation.id");
			countSql.append(" LEFT JOIN ass_customer_relation relation on assList.ass_customer_relation = relation.id where 1=1");
			
			if(null != assChildMember && null != shareType) {
				buffer.append(" and ((customerRelation.ass_child_member=:assChildMember and customerRelation.share_type=:shareType) or (relation.ass_child_member=:assChildMember and assList.ass_child_member!=:assChildMember))");
				countSql.append(" and ((customerRelation.ass_child_member=:assChildMember and customerRelation.share_type=:shareType) or (relation.ass_child_member=:assChildMember and assList.ass_child_member!=:assChildMember))");
			}
			if(null != startDate && !startDate.equals("")) {
				buffer.append(" and assList.create_date >= :startDate");
				countSql.append(" and assList.create_date >= :startDate");
			}
			if(null != endDate && !endDate.equals("")) {
				buffer.append(" and assList.create_date <= :endDate");
				countSql.append(" and assList.create_date <= :endDate");
			}
			if(null != searchValue && !searchValue.equals("")) {
				buffer.append(" and (assList.client_name like:searchValue or customerRelation.theme like:searchValue or assList.single_person like:searchValue)");
				countSql.append(" and (assList.client_name like:searchValue or customerRelation.theme like:searchValue or assList.single_person like:searchValue)");
			}
			/*if(null != shareType) {
				buffer.append(" and customerRelation.share_type=:shareType");
				countSql.append(" and customerRelation.share_type=:shareType");
			}*/
			buffer.append(" and assList.deleted = 0");
			countSql.append(" and assList.deleted = 0");
			buffer.append(" ORDER BY assList.create_date DESC");
			countSql.append(" ORDER BY assList.create_date DESC");
			
			Query query = entityManager.createNativeQuery(buffer.toString());
			Query countQuery = entityManager.createNativeQuery(countSql.toString());
			
			if(null != assChildMember) {
				query.setParameter("assChildMember", assChildMember);
				countQuery.setParameter("assChildMember", assChildMember);
			}
			if (null != startDate && !startDate.equals("")) {
				query.setParameter("startDate", DateUtils.specifyDateZero(sdf.parse(startDate)));
				countQuery.setParameter("startDate", DateUtils.specifyDateZero(sdf.parse(startDate)));
			}
			if(null != endDate && !endDate.equals("")) {
				query.setParameter("endDate",DateUtils.specifyDatetWentyour(sdf.parse(endDate)));
				countQuery.setParameter("endDate",DateUtils.specifyDatetWentyour(sdf.parse(endDate)));
			}
			if(null != searchValue && !searchValue.equals("")) {
				query.setParameter("searchValue","%"+searchValue+"%");
				countQuery.setParameter("searchValue","%"+searchValue+"%");
			}
			if(null != shareType) {
				query.setParameter("shareType",shareType.ordinal());
				countQuery.setParameter("shareType",shareType.ordinal());
			}
			query.unwrap(SQLQuery.class).addEntity(AssList.class).addScalar("pClientName", StringType.INSTANCE)
			.addScalar("pTheme", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssListAndCustomerRelationDto.class));
			
			BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			
			List<AssListAndCustomerRelationDto> list = query.getResultList();
			
			return new Page<AssListAndCustomerRelationDto>(list, total, pageable);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public Page<AssListAndCustomerRelationDto> findPageBySupplier(Supplier supplier,  Date startDate, Date endDate,
			String searchValue, Pageable pageable) {
			StringBuffer findSql=new StringBuffer();
			findSql.append(" SELECT DISTINCT assList.*,relation.admin_name adminName,relation.theme pTheme from ass_list assList  ");
			findSql.append(" LEFT JOIN ass_list_item item ON item.ass_list=assList.id ");
			findSql.append(" LEFT JOIN ass_product product ON product.id=item.ass_product ");
			findSql.append(" LEFT JOIN ass_goods goods on goods.id=product.ass_goods ");
			findSql.append(" LEFT JOIN ass_customer_relation relation ON relation.id=goods.ass_customer_relation ");
			findSql.append(" LEFT JOIN ass_customer_relation customer ON customer.id=relation.source ");
			findSql.append(" LEFT JOIN ass_good_directory directorys ON directorys.id=customer.ass_good_directory ");
			findSql.append(" LEFT JOIN t_supplier suppliers ON suppliers.id=directorys.supplier ");
			
			findSql.append(" LEFT JOIN ass_good_directory directoryst ON directoryst.id=relation.ass_good_directory ");
			findSql.append(" LEFT JOIN t_supplier supplierst ON supplierst.id=directoryst.supplier ");
			findSql.append(" where 1=1 ");
			if (supplier != null) {
				findSql.append(" and (suppliers.id=:supplier  or  (customer.id is null and supplierst.id=:supplier) )");
			}
			if(null != startDate) {
				findSql.append(" and assList.create_date>=:startDate");
			}
			if(null != endDate) {
				findSql.append(" and assList.create_date<=:endDate");
			}
			if(StringUtils.isNotBlank(searchValue)) {
				findSql.append(" and (assList.sn like :searchValue or assList.name like :searchValue or relation.admin_name like :searchValue)");
			}
			findSql.append(" order by assList.create_date desc ");
			
			StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
			countSql.append(findSql);
			countSql.append(" ) datas ");
			
			Query query = entityManager.createNativeQuery(findSql.toString());
			Query countQuery = entityManager.createNativeQuery(countSql.toString());
			
			if (supplier != null) {
				query.setParameter("supplier", supplier.getId());
				countQuery.setParameter("supplier", supplier.getId());
			}
			if(null != startDate) {
				query.setParameter("startDate" , startDate);
				countQuery.setParameter("startDate", startDate);
			}
			if(null != endDate) {
				query.setParameter("endDate" , endDate);
				countQuery.setParameter("endDate", endDate);
			}
			if(StringUtils.isNotBlank(searchValue)) {
				query.setParameter("searchValue" , "%"+searchValue+"%") ;
				countQuery.setParameter("searchValue", "%"+searchValue+"%");
			}
			
			query.unwrap(SQLQuery.class).addEntity(AssList.class).addScalar("pTheme", StringType.INSTANCE)
			.addScalar("adminName", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssListAndCustomerRelationDto.class));
			
			BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			
			List<AssListAndCustomerRelationDto> list=query.getResultList();
			
			return new Page<AssListAndCustomerRelationDto>(list, total, pageable);
	}


	@Override
	public List<AssListAndCustomerRelationDto> findListBySupplier(Supplier supplier, Date startDate, Date endDate,
			String searchValue) {
		StringBuffer findSql=new StringBuffer();
		findSql.append(" SELECT DISTINCT assList.*,relation.admin_name adminName,relation.theme pTheme from ass_list assList  ");
		findSql.append(" LEFT JOIN ass_list_item item ON item.ass_list=assList.id ");
		findSql.append(" LEFT JOIN ass_product product ON product.id=item.ass_product ");
		findSql.append(" LEFT JOIN ass_goods goods on goods.id=product.ass_goods ");
		findSql.append(" LEFT JOIN ass_customer_relation relation ON relation.id=goods.ass_customer_relation ");
		findSql.append(" LEFT JOIN ass_customer_relation customer ON customer.id=relation.source ");
		findSql.append(" LEFT JOIN ass_good_directory directorys ON directorys.id=customer.ass_good_directory ");
		findSql.append(" LEFT JOIN t_supplier suppliers ON suppliers.id=directorys.supplier ");
		
		findSql.append(" LEFT JOIN ass_good_directory directoryst ON directoryst.id=relation.ass_good_directory ");
		findSql.append(" LEFT JOIN t_supplier supplierst ON supplierst.id=directoryst.supplier ");
		findSql.append(" where 1=1 ");
		if (supplier != null) {
			findSql.append(" and (suppliers.id=:supplier  or  (customer.id is null and supplierst.id=:supplier) )");
		}
		if(null != startDate) {
			findSql.append(" and assList.create_date>=:startDate");
		}
		if(null != endDate) {
			findSql.append(" and assList.create_date<=:endDate");
		}
		if(StringUtils.isNotBlank(searchValue)) {
			findSql.append(" and (assList.sn like :searchValue or assList.name like :searchValue or relation.admin_name like :searchValue)");
		}
		findSql.append(" order by assList.create_date desc ");
		
		Query query = entityManager.createNativeQuery(findSql.toString());
		
		if (supplier != null) {
			query.setParameter("supplier", supplier.getId());
		}
		if(null != startDate) {
			query.setParameter("startDate" , startDate);
		}
		if(null != endDate) {
			query.setParameter("endDate" , endDate);
		}
		if(StringUtils.isNotBlank(searchValue)) {
			query.setParameter("searchValue" , "%"+searchValue+"%") ;
		}
		
		query.unwrap(SQLQuery.class).addEntity(AssList.class).addScalar("pTheme", StringType.INSTANCE)
		.addScalar("adminName", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssListAndCustomerRelationDto.class));
		
		List<AssListAndCustomerRelationDto> list=query.getResultList();
		
		return list;
	}
	@Override
	public AssListAndCustomerRelationDto findDetailsById(Long id) {
		StringBuffer findSql=new StringBuffer();
		findSql.append(" SELECT assList.*,relation.admin_name adminName,relation.theme pTheme from ass_list assList  ");
		findSql.append(" LEFT JOIN ass_list_item item ON item.ass_list=assList.id ");
		findSql.append(" LEFT JOIN ass_product product ON product.id=item.ass_product ");
		findSql.append(" LEFT JOIN ass_goods goods on goods.id=product.ass_goods ");
		findSql.append(" LEFT JOIN ass_customer_relation relation ON relation.id=goods.ass_customer_relation ");
		findSql.append(" LEFT JOIN ass_customer_relation customer ON customer.id=relation.source ");
		findSql.append(" where 1=1 ");
		if (id != null) {
			findSql.append(" and assList.id=:id ");
		}
		Query query = entityManager.createNativeQuery(findSql.toString());
		if (id != null) {
			query.setParameter("id", id);
		}
		
		query.unwrap(SQLQuery.class).addEntity(AssList.class).addScalar("pTheme", StringType.INSTANCE)
		.addScalar("adminName", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssListAndCustomerRelationDto.class));
		
		List<AssListAndCustomerRelationDto> list=query.getResultList();
		
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<AssListAndCustomerRelationDto> findDetailsListById(Long[] ids) {
		StringBuffer findSql=new StringBuffer();
		findSql.append(" SELECT DISTINCT assList.*,customer.admin_name adminName,customer.theme pTheme from ass_list assList  ");
		findSql.append(" LEFT JOIN ass_list_item item ON item.ass_list=assList.id ");
		findSql.append(" LEFT JOIN ass_product product ON product.id=item.ass_product ");
		findSql.append(" LEFT JOIN ass_goods goods on goods.id=product.ass_goods ");
		findSql.append(" LEFT JOIN ass_customer_relation relation ON relation.id=goods.ass_customer_relation ");
		findSql.append(" LEFT JOIN ass_customer_relation customer ON customer.id=relation.source ");
		findSql.append(" where 1=1 ");
		if (ids != null) {
			findSql.append(" and assList.id in (:ids) ");
		}
		Query query = entityManager.createNativeQuery(findSql.toString());
		if (ids != null) {
			query.setParameter("ids", Arrays.asList(ids));
		}
		
		query.unwrap(SQLQuery.class).addEntity(AssList.class).addScalar("pTheme", StringType.INSTANCE)
		.addScalar("adminName", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(AssListAndCustomerRelationDto.class));
		
		List<AssListAndCustomerRelationDto> list=query.getResultList();
		return list;
	}
}

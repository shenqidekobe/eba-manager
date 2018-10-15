package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.NeedDao;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Area;
import com.microBusiness.manage.entity.CustomerRelation;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.NeedProduct;
import com.microBusiness.manage.entity.Need.NeedStatus;
import com.microBusiness.manage.entity.ShopType;
import com.microBusiness.manage.entity.SupplierAssignRelation;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Need.Status;
import com.microBusiness.manage.entity.Need.Type;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class NeedDaoImpl extends BaseDaoImpl<Need, Long> implements NeedDao {
    @Override
    public Page<Need> findPage(Pageable pageable, Need need , String searchName, Date startDate, Date endDate) {
        try {
        	Supplier supplier = need.getSupplier();
        	Need.Type type = need.getType();
        	String tel=need.getTel();
        	StringBuffer sqlSelect = new StringBuffer("select DISTINCT need.* from t_need need where 1=1");
        	StringBuffer sqlCount = new StringBuffer("select count(DISTINCT need.id) from t_need need where 1=1");
        	
        	StringBuffer buffer = new StringBuffer();
        	if(supplier != null) {
        		buffer.append(" and need.supplier = :supplier");
        	}
        	buffer.append(" and need.deleted = 0  and need.id not in(");
        	buffer.append("select need.id needId from t_need need INNER JOIN t_supply_need supplyNeed on  need.id=supplyNeed.need WHERE 1=1");
        	if(supplier != null) {
        		buffer.append(" and need.supplier = :supplier");
        	}
        	buffer.append(" and need.deleted = 0 and supplyNeed.deleted = 0");
        	if(type != null) {
        		buffer.append(" and need.type = :type");
        	}
        	buffer.append(")");
        	if(type != null) {
        		buffer.append(" and need.type = :type");
        	}
        	if(startDate != null) {
        		buffer.append(" and need.create_date >= :startDate");
        	}
        	if(endDate != null) {
        		buffer.append(" and need.create_date <= :endDate");
        	}
        	if(searchName != null && searchName != "") {
        		buffer.append(" and (need.name like :searchName or need.client_num like :searchName or need.user_name like :searchName)");
        	}
        	if (StringUtils.isNotBlank(tel)) {
        		buffer.append(" and need.tel like :tel");
			}
        	
        	buffer.append(" ORDER BY CONVERT(name using gbk) collate gbk_chinese_ci ASC");
        	
        	sqlSelect.append(buffer);
        	sqlCount.append(buffer);
        	
        	Query query = entityManager.createNativeQuery(sqlSelect.toString(), Need.class);
        	Query count = entityManager.createNativeQuery(sqlCount.toString());
        	
        	if(supplier != null) {
        		query.setParameter("supplier", supplier.getId());
        		count.setParameter("supplier", supplier.getId());
        	}
        	if(type != null) {
        		query.setParameter("type", type.ordinal());
        		count.setParameter("type", type.ordinal());
        	}
        	if(startDate != null) {
        		query.setParameter("startDate", startDate);
        		count.setParameter("startDate", startDate);
        	}
        	if(endDate != null) {
        		query.setParameter("endDate", endDate);
        		count.setParameter("endDate", endDate);
        	}
        	if(searchName != null && searchName != "") {
        		query.setParameter("searchName", "%"+searchName+"%");
        		count.setParameter("searchName", "%"+searchName+"%");
        	}
        	if (StringUtils.isNotBlank(tel)) {
        		query.setParameter("tel", "%"+tel+"%");
        		count.setParameter("tel", "%"+tel+"%");
        	}
        	
        	BigInteger tempTotal = (BigInteger) count.getSingleResult();
    		int total = tempTotal.intValue();
    		
    		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
    		if (totalPages < pageable.getPageNumber()) {
    			pageable.setPageNumber(totalPages);
    		}
    		
    		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
    		query.setMaxResults(pageable.getPageSize());
        	List<Need> list = query.getResultList();
    		return new Page<Need>(list, total, pageable);
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
    	
    }


    @Override
    public Page<Need> findPage(Pageable pageable, Need need, Date startDate, Date endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != need.getSupplier()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), need.getSupplier()));
        }

        if(null != need.getNeedSuppliers()){
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.join("needSuppliers" , JoinType.LEFT)).value(need.getNeedSuppliers()));
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);

    }

    @Override
    public List<Need> findBySupplier(Supplier supplier, Need.Type type) {
         try {
             String jpql = "select need from Need need where need.supplier =:supplier and need.type =:type";
             return entityManager.createQuery(jpql, Need.class).setParameter("supplier", supplier).setParameter("type", type).getResultList();
         } catch (NoResultException e) {
             return null;
         }
    }

    @Override
    public Need findByTel(String tel) {
        if (StringUtils.isEmpty(tel)) {
            return null;
        }
        try {
            String jpql = "select need from Need need where lower(need.tel) = lower(:tel)";
            return entityManager.createQuery(jpql, Need.class).setParameter("tel", tel).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


	@Override
	public Page<Need> findPage(Pageable pageable, Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
	}

    @Override
    public Page<Need> findPage(Pageable pageable, Supplier supplier , Need.Type type,String searchName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }

        if (null != type) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }
        if(StringUtils.isNotEmpty(searchName)) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name") , "%"+searchName+"%")));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("needStatus"), Need.NeedStatus.available.ordinal()));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
    
    @Override
    public Page<Need> findPage(Pageable pageable, Supplier supplier , Need.Type type,String searchName,List<Need.NeedStatus> needStatus) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != supplier) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
        }

        if (null != type) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }
        if(StringUtils.isNotEmpty(searchName)) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name") , "%"+searchName+"%")));
        }
        if (needStatus != null) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.in(root.get("needStatus")).value(needStatus));
		}
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.and(root.get("shopType").isNull()));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }


	@Override
	public Need modifyCheckTel(String tel , Long id) {
		if (StringUtils.isEmpty(tel)) {
            return null;
        }
        try {
            String jpql = "select need from Need need where lower(need.tel) = lower(:tel) and lower(need.id) != lower(:id)";
            return entityManager.createQuery(jpql, Need.class).setParameter("tel", tel).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
	}


	@Override
	public Member findByMember(Long needId) {
		StringBuffer findSql = new StringBuffer("select member.* from t_need need inner join xx_member member on member.need = need.id");
		findSql.append(" where need.id =:id");
		Query query = entityManager.createNativeQuery(findSql.toString() , Member.class) ;
		query.setParameter("id" , needId) ;
		Member member =(Member) query.getSingleResult();
		return member;
	}


	@Override
	public Page<Need> findPage(Pageable pageable, Supplier supplier, SupplierSupplier supplyRelation, Type type,
			Need.NeedStatus needStatus , Need.Status status,ShopType shopType) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        Predicate restrictions1 = criteriaBuilder.conjunction();
        if (null != supplier) {
        	Join<Need , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(supplierJoin.<Long>get("id"), supplier.getId()));
        }
        if (null != supplyRelation) {
        	Join<Need , SupplierAssignRelation> relationJoin = root.join("supplierAssignRelations" , JoinType.LEFT);
        	restrictions1 = criteriaBuilder.and(restrictions1, criteriaBuilder.equal(relationJoin.get("supplyRelation").get("id"), supplyRelation.getId()));
        	relationJoin.on(restrictions1);
        	if(null != status) {
        		if(status == Need.Status.on) {
            		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(relationJoin.get("id")));
            	}else {
            		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(relationJoin.get("id")));
            	}
        	}
        }
        if (null != type) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }
        if (null != needStatus) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("needStatus"), needStatus));
        }
        if(null != shopType) {
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shopType"), shopType));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
	}


	@Override
	public boolean telExists(String tel, Supplier supplier) {
		if (StringUtils.isEmpty(tel)) {
			return false;
		}
		String jpql = "select count(*) from Need need where lower(need.tel) = lower(:tel) and need.supplier=:supplier";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("tel", tel).setParameter("supplier", supplier).getSingleResult();
		return count > 0;
	}
	
	@Override
	public boolean nameExists(String name, Supplier supplier) {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		String jpql = "select count(*) from Need need where need.name =:name and need.supplier=:supplier";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("name", name).setParameter("supplier", supplier).getSingleResult();
		return count > 0;
	}
	
	@Override
	public Need findByTelSupplier(String tel, Supplier supplier) {
		if (StringUtils.isEmpty(tel)) {
			return null;
		}
		try {
			String jpql = "select need from Need need where lower(need.tel) = lower(:tel) and need.supplier=:supplier";
			return entityManager.createQuery(jpql, Need.class).setParameter("tel", tel).setParameter("supplier", supplier).getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Need findNeedByMemberSupplier(Supplier supplier, Member member) {
		StringBuffer findSql = new StringBuffer("select need.* from t_need need ");
		findSql.append(" where need.member =:member")
				.append(" and need.supplier =:supplier");
		Query query = entityManager.createNativeQuery(findSql.toString() , Need.class) ;
		query.setParameter("member" , member.getId())
			.setParameter("supplier" , supplier.getId());
		List<Need> list = query.getResultList();
		Need need = null;
		if(!list.isEmpty()){
			need = list.get(0);
		}
		return need;
	}


	@Override
	public Page<Need> findPage(AssChildMember assChildMember, Pageable pageable) {
		try {
			StringBuffer buffer = new StringBuffer("select need.* from ass_child_member assChildMember");
			buffer.append(" INNER JOIN xx_admin admin on assChildMember.admin = admin.id");
			buffer.append(" INNER JOIN t_need need on admin.id = need.admin where 1=1");
			if(null != assChildMember) {
				buffer.append(" and assChildMember.id = :id");
			}
			buffer.append(" ORDER BY need.create_date desc");
			StringBuffer countSql = new StringBuffer("select count(need.id) from ass_child_member assChildMember");
			countSql.append(" INNER JOIN xx_admin admin on assChildMember.admin = admin.id");
			countSql.append(" INNER JOIN t_need need on admin.id = need.admin where 1=1");
			if(null != assChildMember) {
				countSql.append(" and assChildMember.id = :id");
			}
			
			Query query = entityManager.createNativeQuery(buffer.toString(), Need.class);
			Query countQuery = entityManager.createNativeQuery(countSql.toString());
			if(null != assChildMember) {
				query.setParameter("id", assChildMember.getId());
			}
			if(null != assChildMember) {
				countQuery.setParameter("id", assChildMember.getId());
			}
			
			BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			
			List<Need> list = query.getResultList();
			return new Page<Need>(list, total, pageable);
		} catch (Exception e) {
			return null;
		}
		
	}


	@Override
	public List<Need> findList(AssChildMember assChildMember) {
		if(null == assChildMember.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select need.* from t_need need INNER JOIN xx_admin admin on need.admin = admin.id");
			buffer.append(" INNER JOIN ass_child_member childMemer on admin.id = childMemer.admin where 1=1");
			buffer.append(" and childMemer.id = :id");
			buffer.append(" and need.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString() , Need.class);
			query.setParameter("id",assChildMember.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}



	@Override
	public boolean existTel(Supplier supplier, String tel, ShopType shopType) {
		if (StringUtils.isEmpty(tel) || supplier == null || shopType == null) {
			return false;
		}
		String jpql = "select count(*) from Need need where need.tel = :tel and need.supplier = :supplier and need.shopType = :shopType";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("tel", tel)
				.setParameter("supplier", supplier).setParameter("shopType", shopType).getSingleResult();
		return count > 0;
	}


	@Override
	public List<Need> findList(Supplier supplier, ShopType shopType) {
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT need.* from t_supply_need supplyNeed INNER JOIN t_need need on supplyNeed.need = need.id where 1=1");
			buffer.append(" and supplyNeed.supplier = :supplier");
			if(null != shopType) {
				buffer.append(" and need.shop_type = :shopType");
			}
			buffer.append(" and supplyNeed.deleted = 0");
			buffer.append(" and supplyNeed.status =").append(SupplyNeed.Status.SUPPLY.ordinal());
			buffer.append(" ORDER BY CONVERT(need.name using gbk) collate gbk_chinese_ci ASC ");
			
			Query query = entityManager.createNativeQuery(buffer.toString(), Need.class);
			query.setParameter("supplier", supplier.getId());
			if(null != shopType) {
				query.setParameter("shopType", shopType.ordinal());
			}
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}



	@Override
	public List<Need> findByList(Supplier supplier, Type type) {
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT need.* from t_need need where need.supplier = :supplier and need.deleted = 0 and need.id not in(");
			buffer.append(" select need.id needId from t_need need");
			buffer.append(" INNER JOIN t_supply_need supplyNeed on  need.id=supplyNeed.need");
			buffer.append(" WHERE need.supplier = :supplier and need.deleted = 0 and supplyNeed.deleted = 0 and need.type = :type");
			buffer.append(" ) and need.type = :type ORDER BY CONVERT(name using gbk) collate gbk_chinese_ci ASC");
			Query query = entityManager.createNativeQuery(buffer.toString(), Need.class);
			query.setParameter("supplier", supplier.getId());
			query.setParameter("type", type.ordinal());
			return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
	}


	@Override
	public Need find(String clientNum , Supplier supplier) {
		try {
			String sql = "select * from t_need where client_num = :clientNum and supplier = :supplier and deleted = 0";
			Query query = entityManager.createNativeQuery(sql, Need.class);
			query.setParameter("clientNum", clientNum);
			query.setParameter("supplier", supplier.getId());
			return (Need) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}


	@Override
	public Page<Need> findByPage(Pageable pageable, Need need,
			String searchName, Date startDate, Date endDate) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != need.getSupplier()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), need.getSupplier()));
        }
        if (null != need.getType()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), need.getType()));
        }
        if (startDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), startDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
        if(StringUtils.isNotEmpty(searchName)) {
        	Join<Need, Area> areaJoin = root.join("area" , JoinType.LEFT);
        	Join<Need , Admin> adminJoin = root.join("admin" , JoinType.LEFT);
        	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(adminJoin.<String>get("name") , "%"+searchName+"%") ,criteriaBuilder.like(root.<String>get("name") , "%"+searchName+"%") , criteriaBuilder.like(root.<String>get("userName"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("tel"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("address"), "%"+searchName+"%"), criteriaBuilder.like(areaJoin.<String>get("fullName"), "%"+searchName+"%"), criteriaBuilder.like(root.<String>get("clientNum"), "%"+searchName+"%")));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
	}


	@Override
	public boolean nameExists(String name, Supplier supplier, Need need) {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		StringBuffer buffer = new StringBuffer("select count(*) from t_need need where need.supplier = :supplier and need.name = :name");
		if(need != null) {
			buffer.append(" and need.id != :need");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		query.setParameter("supplier", supplier.getId());
		query.setParameter("name", name);
		if(need != null) {
			query.setParameter("need", need.getId());
		}
		Integer count=((BigInteger) query.getSingleResult()).intValue();
		return count > 0;
	}

}

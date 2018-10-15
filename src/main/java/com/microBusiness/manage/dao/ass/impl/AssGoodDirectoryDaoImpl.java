package com.microBusiness.manage.dao.ass.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ass.AssGoodDirectoryDao;
import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.ass.AssChildMember;
import com.microBusiness.manage.entity.ass.AssGoodDirectory;

@Repository("assGoodDirectoryDaoImpl")
public class AssGoodDirectoryDaoImpl extends BaseDaoImpl<AssGoodDirectory,Long> implements AssGoodDirectoryDao{

	@Override
	public Page<AssGoodDirectory> findPage(String theme, Supplier supplier, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssGoodDirectory> criteriaQuery = criteriaBuilder.createQuery(AssGoodDirectory.class);
		Root<AssGoodDirectory> root = criteriaQuery.from(AssGoodDirectory.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (StringUtils.isNotBlank(theme)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("theme") , "%"+theme+"%"));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Page<AssGoodDirectory> findPage(AssChildMember assChildMember,
			Pageable pageable) {
		try {
			StringBuffer buffer = new StringBuffer("select goodDirectory.* from ass_good_directory goodDirectory");
			buffer.append(" INNER JOIN t_supplier supplier on goodDirectory.supplier = supplier.id");
			buffer.append(" INNER JOIN xx_admin admin on supplier.id = admin.supplier");
			buffer.append(" INNER JOIN ass_child_member childMember on admin.id = childMember.admin where 1=1");
			buffer.append(" and childMember.id = :id");
			buffer.append(" and goodDirectory.deleted = 0");
			buffer.append(" ORDER BY goodDirectory.create_date DESC");
			
			StringBuffer countSql = new StringBuffer("select count(*) from ass_good_directory goodDirectory");
			countSql.append(" INNER JOIN t_supplier supplier on goodDirectory.supplier = supplier.id");
			countSql.append(" INNER JOIN xx_admin admin on supplier.id = admin.supplier");
			countSql.append(" INNER JOIN ass_child_member childMember on admin.id = childMember.admin where 1=1");
			countSql.append(" and childMember.id = :id");
			countSql.append(" and goodDirectory.deleted = 0");
			
			Query query = entityManager.createNativeQuery(buffer.toString() , AssGoodDirectory.class);
			Query countQuery = entityManager.createNativeQuery(countSql.toString());
			
			query.setParameter("id",assChildMember.getId());
			countQuery.setParameter("id",assChildMember.getId());
			
			BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
			int total = tempTotal.intValue();
			
			int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
			if (totalPages < pageable.getPageNumber()) {
				pageable.setPageNumber(totalPages);
			}
			
			query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			
			List<AssGoodDirectory> list = query.getResultList();
			return new Page<AssGoodDirectory>(list, total, pageable);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<AssGoodDirectory> findList(AssChildMember assChildMember) {
		if(null == assChildMember.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select goodDirectory.* from ass_good_directory goodDirectory");
			buffer.append(" INNER JOIN t_supplier supplier on goodDirectory.supplier = supplier.id");
			buffer.append(" INNER JOIN xx_admin admin on supplier.id = admin.supplier");
			buffer.append(" INNER JOIN ass_child_member childMember on admin.id = childMember.admin where 1=1");
			buffer.append(" and childMember.id = :id");
			buffer.append(" and goodDirectory.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString() , AssGoodDirectory.class);
			query.setParameter("id",assChildMember.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public boolean themeExists(String theme, Supplier supplier) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AssGoodDirectory> criteriaQuery = criteriaBuilder.createQuery(AssGoodDirectory.class);
		Root<AssGoodDirectory> root = criteriaQuery.from(AssGoodDirectory.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("deleted"), false));
		if (supplier != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"), supplier));
		}
		if (StringUtils.isNotBlank(theme)) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("theme"), theme));
		}
		criteriaQuery.where(restrictions);
		List<AssGoodDirectory> list=this.findList(criteriaQuery, null, null, null, null);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<AssGoodDirectory> findList(Goods goods) {
		if(null == goods || null == goods.getId()) {
			return null;
		}
		try {
			StringBuffer buffer = new StringBuffer("select DISTINCT goodDirectory.* from ass_good_directory goodDirectory");
			buffer.append(" INNER JOIN ass_good_directory_goods directoryGoods on goodDirectory.id = directoryGoods.ass_good_directory");
			buffer.append(" INNER JOIN xx_goods goods on directoryGoods.goods = goods.id where 1=1");
			buffer.append(" and goods.id=:goods and goodDirectory.deleted = 0");
			Query query = entityManager.createNativeQuery(buffer.toString() , AssGoodDirectory.class);
			query.setParameter("goods", goods.getId());
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
		
	}

}

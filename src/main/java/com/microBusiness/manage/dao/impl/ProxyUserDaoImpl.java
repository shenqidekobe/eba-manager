/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.ProxyUserDao;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Need;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.ProxyUser;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.SupplierSupplier;
import com.microBusiness.manage.entity.SupplyNeed;
import com.microBusiness.manage.entity.Types;

@Repository("proxyUserDaoImpl")
public class ProxyUserDaoImpl extends BaseDaoImpl<ProxyUser, Long> implements ProxyUserDao {

	public List<ProxyUser> findRoots(Integer count) {
		String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.parent is null order by ProxyUser.order asc";
		TypedQuery<ProxyUser> query = entityManager.createQuery(jpql, ProxyUser.class);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count) {
		if (ProxyUser == null || ProxyUser.getParent() == null) {
			return Collections.emptyList();
		}
		TypedQuery<ProxyUser> query;
		if (recursive) {
			String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.id in (:ids) order by ProxyUser.grade asc";
			query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("ids", Arrays.asList(ProxyUser.getParentIds()));
		} else {
			String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser = :ProxyUser";
			query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("ProxyUser", ProxyUser.getParent());
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	/**
	 *
	 * @param ProxyUser
	 * @param recursive 是否递归
	 * @param count
	 * @return
	 */
	public List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count) {
		TypedQuery<ProxyUser> query;
		if (recursive) {
			if (ProxyUser != null) {
				String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.treePath like :treePath order by ProxyUser.grade asc, ProxyUser.order asc";
				query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("treePath", "%" + ProxyUser.TREE_PATH_SEPARATOR + ProxyUser.getId() + ProxyUser.TREE_PATH_SEPARATOR + "%");
			} else {
				String jpql = "select ProxyUser from ProxyUser ProxyUser order by ProxyUser.grade asc, ProxyUser.order asc";
				query = entityManager.createQuery(jpql, ProxyUser.class);
			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProxyUser> result = query.getResultList();
			sort(result);
			return result;
		} else {
			String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.parent = :parent order by ProxyUser.order asc";
			query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("parent", ProxyUser);
			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}

	private void sort(List<ProxyUser> productCategories) {
		if (CollectionUtils.isEmpty(productCategories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
		for (ProxyUser ProxyUser : productCategories) {
			orderMap.put(ProxyUser.getId(), ProxyUser.getOrder());
		}
		Collections.sort(productCategories, new Comparator<ProxyUser>() {
			@Override
			public int compare(ProxyUser ProxyUser1, ProxyUser ProxyUser2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(ProxyUser1.getParentIds(), ProxyUser1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(ProxyUser2.getParentIds(), ProxyUser2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(ProxyUser1.getGrade(), ProxyUser2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}


	@Override
	public List<ProxyUser> findRoots(Integer count, Supplier supplier) {
		String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.parent is null and ProxyUser.supplier=:supplier order by ProxyUser.order asc";
		TypedQuery<ProxyUser> query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("supplier", supplier);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Override
	public List<ProxyUser> findParents(ProxyUser ProxyUser, boolean recursive, Integer count, Supplier supplier) {
		return null;
	}

	@Override
	public List<ProxyUser> findChildren(ProxyUser ProxyUser, boolean recursive, Integer count, Supplier supplier , String searchName) {
		TypedQuery<ProxyUser> query;
		StringBuffer queryStr = new StringBuffer() ;
		if (recursive) {
			if (ProxyUser != null) {
				queryStr.append("select ProxyUser from ProxyUser ProxyUser where ProxyUser.deleted=0 and ProxyUser.treePath like :treePath") ;
				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and ProxyUser.name like :searchName");
				}
				//queryStr.append(" and ProxyUser.types=0");

				queryStr.append(" order by ProxyUser.grade asc, ProxyUser.order asc");

				//String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.treePath like :treePath order by ProxyUser.grade asc, ProxyUser.order asc";

				query = entityManager.createQuery(queryStr.toString(), ProxyUser.class).setParameter("treePath", "%" + ProxyUser.TREE_PATH_SEPARATOR + ProxyUser.getId() + ProxyUser.TREE_PATH_SEPARATOR + "%");

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			} else {
				//String jpql = "select ProxyUser from ProxyUser ProxyUser order by ProxyUser.grade asc, ProxyUser.order asc";

				queryStr.append("select ProxyUser from ProxyUser ProxyUser where 1=1 and ProxyUser.deleted=0") ;

				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and ProxyUser.name like :searchName");
				}

				queryStr.append(" order by ProxyUser.grade asc, ProxyUser.order asc");

				query = entityManager.createQuery(queryStr.toString() , ProxyUser.class);

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProxyUser> result = query.getResultList();
			sort(result);
			return result;
		} else {
			//String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.parent = :parent order by ProxyUser.order asc";

			queryStr.append("select ProxyUser from ProxyUser ProxyUser where 1=1 and  ProxyUser.deleted=0 and ProxyUser.parent = :parent") ;

			if(null != supplier){
				queryStr.append(" and supplier=:supplier");
			}
			if(null != searchName) {
				queryStr.append(" and ProxyUser.name like :searchName");
			}
			//queryStr.append(" and ProxyUser.types=0");

			queryStr.append(" order by ProxyUser.grade asc, ProxyUser.order asc");


			query = entityManager.createQuery(queryStr.toString(), ProxyUser.class).setParameter("parent", ProxyUser);

			if(null != supplier){
				query.setParameter("supplier" , supplier);
			}
			if(null != searchName) {
				query.setParameter("searchName" , "%" + searchName + "%");
			}

			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}


	

	

	

	@SuppressWarnings("unchecked")
	@Override
	public List<ProxyUser> findByAllSupplier(Long supplierId) {
		StringBuffer findSql = new StringBuffer("select proxyUser.* from t_proxy_user proxyUser");
		findSql.append(" where 1=1 and proxyUser.deleted=0 and proxyUser.supplier=:supplierId");
		Query query = entityManager.createNativeQuery(findSql.toString() , ProxyUser.class) ;
		if(null != supplierId){
			query.setParameter("supplierId" , supplierId);
		}
		List<ProxyUser> result = query.getResultList() ;
		return result;
	}

	

	@Override
	public List<ProxyUser> findLike(Long ProxyUserId) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProxyUser> criteriaQuery = criteriaBuilder.createQuery(ProxyUser.class);
		Root<ProxyUser> root = criteriaQuery.from(ProxyUser.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (ProxyUserId != null) {
			restrictions = criteriaBuilder.and(criteriaBuilder.equal(root.get("treePath"), "%"+ProxyUserId+"%"));
		}
		criteriaQuery.where(restrictions);
		return findList(criteriaQuery, null, null, null, null);
	}

	

	@Override
	public List<ProxyUser> findChildrenList(
			ProxyUser ProxyUser, boolean recursive, Integer count,
			Supplier supplier, String searchName) {
		TypedQuery<ProxyUser> query;
		StringBuffer queryStr = new StringBuffer() ;
		if (recursive) {
			if (ProxyUser != null) {
				queryStr.append("select proxyUser from ProxyUser proxyUser where proxyUser.deleted=0 and proxyUser.treePath like :treePath") ;
				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and proxyUser.name like :searchName");
				}
				//queryStr.append(" and ProxyUser.types=0");
				
				queryStr.append(" order by proxyUser.grade asc, proxyUser.order asc");

				//String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.treePath like :treePath order by ProxyUser.grade asc, ProxyUser.order asc";

				query = entityManager.createQuery(queryStr.toString(), ProxyUser.class).setParameter("treePath", "%" + ProxyUser.TREE_PATH_SEPARATOR + ProxyUser.getId() + ProxyUser.TREE_PATH_SEPARATOR + "%");

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			} else {
				//String jpql = "select ProxyUser from ProxyUser ProxyUser order by ProxyUser.grade asc, ProxyUser.order asc";

				queryStr.append("select proxyUser from ProxyUser proxyUser where 1=1 and proxyUser.deleted=0") ;

				if(null != supplier){
					queryStr.append(" and supplier=:supplier");
				}
				if(null != searchName) {
					queryStr.append(" and proxyUser.name like :searchName");
				}
				queryStr.append(" order by proxyUser.grade asc, proxyUser.order asc");

				query = entityManager.createQuery(queryStr.toString() , ProxyUser.class);

				if(null != supplier){
					query.setParameter("supplier" , supplier);
				}
				if(null != searchName) {
					query.setParameter("searchName" , "%" + searchName + "%");
				}

			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<ProxyUser> result = query.getResultList();
			sort(result);
			return result;
		} else {
			//String jpql = "select ProxyUser from ProxyUser ProxyUser where ProxyUser.parent = :parent order by ProxyUser.order asc";

			queryStr.append("select proxyUser from ProxyUser proxyUser where 1=1 and  proxyUser.deleted=0 and proxyUser.parent = :parent") ;

			if(null != supplier){
				queryStr.append(" and supplier=:supplier");
			}
			if(null != searchName) {
				queryStr.append(" and proxyUser.name like :searchName");
			}

			queryStr.append(" order by proxyUser.grade asc, proxyUser.order asc");


			query = entityManager.createQuery(queryStr.toString(), ProxyUser.class).setParameter("parent", ProxyUser);

			if(null != supplier){
				query.setParameter("supplier" , supplier);
			}
			if(null != searchName) {
				query.setParameter("searchName" , "%" + searchName + "%");
			}

			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}
	
	

	

	@Override
	public List<ProxyUser> findByParent(Supplier supplier, ProxyUser parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.supplier =:supplier and proxyUser.deleted=0 and proxyUser.parent =:parent and proxyUser.name =:name";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("parent", parent).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			} else {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.supplier =:supplier and proxyUser.deleted=0 and proxyUser.parent is null and proxyUser.name =:name";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("supplier", supplier).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<ProxyUser> findByParent(Member member, ProxyUser parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.member =:member and proxyUser.deleted=0 and proxyUser.parent =:parent and proxyUser.name =:name";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("parent", parent).setParameter("member", member).setParameter("name", name).getResultList();
			} else {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.member =:member and proxyUser.deleted=0 and proxyUser.parent is null and proxyUser.name =:name";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("member", member).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProxyUser> findTree(Member member, String searchName, ProxyUser proxyUser) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_proxy_user proxyUser where 1=1 ");
//		if(null != member) {
//			buffer.append(" and proxyUser.member = :member");
//		}
		if(null != proxyUser) {
			buffer.append(" and proxyUser.tree_path like :treePath");
		}
		if(null != searchName) {
			buffer.append(" and proxyUser.name like :searchName");
		}
		buffer.append(" and proxyUser.deleted = 0");
		buffer.append(" order by proxyUser.grade ASC, proxyUser.orders ASC");
		
		Query query = entityManager.createNativeQuery(buffer.toString(), ProxyUser.class);
//		if(null != member) {
//			query.setParameter("member", member.getId());
//		}
		if(null != proxyUser) {
			query.setParameter("treePath", "%" + ProxyUser.TREE_PATH_SEPARATOR + proxyUser.getId() + ProxyUser.TREE_PATH_SEPARATOR + "%");
		}
		if(null != searchName) {
			query.setParameter("searchName", "%" + searchName + "%");
		}
		List<ProxyUser> result = query.getResultList();
		sort(result);
		return result;
	}


	
	@Override
	public List<ProxyUser> findRootByMember(Member member) {
		String jpql = "select proxyUser from ProxyUser proxyUser where  proxyUser.member = :member and proxyUser.parent is null order by proxyUser.order asc";
		TypedQuery<ProxyUser> query = entityManager.createQuery(jpql, ProxyUser.class).setParameter("member" , member);
		return query.getResultList();
	}

	
	@Override
	public List<ProxyUser> findByParent(Member member,
			ProxyUser parent, String name, Types types) {
		try {
			if (parent != null) {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.member =:member and proxyUser.deleted=0 and proxyUser.parent =:parent and proxyUser.name =:name and proxyUser.types=:types";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("parent", parent).setParameter("member", member).setParameter("name", name).setParameter("types", types).getResultList();
			} else {
				String jpql = "select proxyUser from ProxyUser proxyUser where proxyUser.member =:member and proxyUser.deleted=0 and proxyUser.parent is null and proxyUser.name =:name and proxyUser.types=:types";
				return entityManager.createQuery(jpql, ProxyUser.class).setParameter("member", member).setParameter("name", name).setParameter("types", types).getResultList();
			}
		} catch (Exception e) {
			return null;
		}
	}

}
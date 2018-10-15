package com.microBusiness.manage.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.ShopDao;
import com.microBusiness.manage.entity.Cart;
import com.microBusiness.manage.entity.CartItem;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.Shop;

@Repository("shopDaoImpl")
public class ShopDaoImpl extends BaseDaoImpl<Shop, Long> implements ShopDao{

	@Override
	public Page<Shop> findPage(Pageable pageable, Member member) {
		StringBuffer findSql = new StringBuffer(" select DISTINCT shop.* from t_shop shop ");
		findSql.append(" left join t_hosting_shop host on host.shop=shop.id ");
		findSql.append(" where 1=1 ");
		findSql.append(" and (shop.member=:member or host.by_member=:member) ");
		findSql.append(" order by create_date asc ");
		
		StringBuffer countSql=new StringBuffer(" select COUNT(datas.id) from ( ");
		countSql.append(findSql);
		countSql.append(" ) datas ");
		
		Query query = entityManager.createNativeQuery(findSql.toString(),Shop.class);
		Query countQuery = entityManager.createNativeQuery(countSql.toString());
		
		query.setParameter("member", member.getId());
		countQuery.setParameter("member", member.getId());
		
		BigInteger tempTotal = (BigInteger) countQuery.getSingleResult();
		int total = tempTotal.intValue();
		
		int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
		if (totalPages < pageable.getPageNumber()) {
			pageable.setPageNumber(totalPages);
		}
		
		query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		
		List<Shop> list=query.getResultList();
		
		return new Page<Shop>(list, total, pageable);
	}

	@Override
	public List<Shop> findList(Member member) {
		StringBuffer findSql = new StringBuffer(" select DISTINCT shop.* from t_shop shop ");
		findSql.append(" left join t_hosting_shop host on host.shop=shop.id ");
		findSql.append(" where 1=1 ");
		findSql.append(" and (shop.member=:member or host.by_member=:member) ");
		Query query = entityManager.createNativeQuery(findSql.toString(),Shop.class);
		query.setParameter("member", member.getId());
		return query.getResultList();
	}

	@Override
	public boolean exitShopNameByMember(Member member, String name, Shop shop) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Shop> criteriaQuery = criteriaBuilder.createQuery(Shop.class);
		Root<Shop> root = criteriaQuery.from(Shop.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (member != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
		}
		if (name != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("name"), name));
		}
		if (shop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(root, shop));
		}
		
		criteriaQuery.where(restrictions);
		List<Shop> list=super.findList(criteriaQuery, null, null, null, null);
		if (list != null && list.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Shop> findShopList(Member member) {
		 try {
		     String jpql = "select shop from Shop shop where shop.member =:member";
		     return entityManager.createQuery(jpql, Shop.class).setParameter("member", member).getResultList();
		 } catch (NoResultException e) {
			 e.printStackTrace();
		     return null;
		 }
	}

	@Override
	public List<Shop> findList(Member member, Product product) {
		StringBuffer sql = new StringBuffer("select shop1.* from t_shop shop1 where 1=1");
		if(member != null) {
			sql.append(" and shop1.member =:member");
		}
		sql.append(" and shop1.id not in (");
		sql.append("select DISTINCT shop.id from t_shop shop");
		sql.append(" LEFT JOIN t_need_shop_product needShopProduct on needShopProduct.shop = shop.id where 1=1");
		if(member != null) {
			sql.append(" and shop.member =:member");
		}
		if(product != null) {
			sql.append(" and needShopProduct.products =:product");
		}
		sql.append(")");
		Query query = entityManager.createNativeQuery(sql.toString(), Shop.class);
		if(member != null) {
			query.setParameter("member", member.getId());
		}
		if(product != null) {
			query.setParameter("product", product.getId());
		}
		List<Shop> list = query.getResultList();
		return query.getResultList();
	}

	@Override
	public Integer count(Member member, Product product, Shop shop) {
		StringBuffer buffer = new StringBuffer("select count(*) from t_shop shop");
		buffer.append(" LEFT JOIN t_need_shop_product needShopProduct on needShopProduct.shop = shop.id where 1=1");
		if(member != null) {
			buffer.append(" and shop.member = :member");
		}
		if(product != null) {
			buffer.append(" and needShopProduct.products = :product");
		}
		if(shop != null) {
			buffer.append(" and shop.id = :shop");
		}
		Query query = entityManager.createNativeQuery(buffer.toString());
		if(member != null) {
			query.setParameter("member", member.getId());
		}
		if(product != null) {
			query.setParameter("product", product.getId());
		}
		if(shop != null) {
			query.setParameter("shop", shop.getId());
		}
		BigInteger count = (BigInteger) query.getSingleResult();
		return count.intValue();
	}

}

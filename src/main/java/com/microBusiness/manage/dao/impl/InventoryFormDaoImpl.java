package com.microBusiness.manage.dao.impl;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.microBusiness.manage.Page;
import com.microBusiness.manage.Pageable;
import com.microBusiness.manage.dao.InventoryFormDao;
import com.microBusiness.manage.entity.ChildMember;
import com.microBusiness.manage.entity.InventoryForm;
import com.microBusiness.manage.entity.Member;
import com.microBusiness.manage.entity.SfIfStatus;
import com.microBusiness.manage.entity.Shop;

import org.springframework.stereotype.Repository;

@Repository("inventoryFormDaoImpl")
public class InventoryFormDaoImpl extends BaseDaoImpl<InventoryForm, Long> implements InventoryFormDao {

	@Override
	public Page<InventoryForm> findPage(Pageable pageable, String inventoryCode, Shop shop, SfIfStatus status) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<InventoryForm> criteriaQuery = criteriaBuilder.createQuery(InventoryForm.class);
		Root<InventoryForm> root = criteriaQuery.from(InventoryForm.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if(inventoryCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("inventoryCode"), "%"+inventoryCode+"%"));
		}
		if (status != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shop"), shop));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	@Override
	public Long getCount(Member member) {
		StringBuffer sql = new StringBuffer("select count(1) counts from t_inventory_from where date(create_date) = curdate() and member =:menber");
		Query query = entityManager.createNativeQuery(sql.toString()).setParameter("menber" , member.getId());
		String count = query.getSingleResult().toString();
		
		return Long.valueOf(count)+1;
	}

	@Override
	public boolean exists(Shop shop, SfIfStatus sfIfStatus) {
		String jpql = "select count(*) from InventoryForm inventoryForm where inventoryForm.shop=:shop and inventoryForm.status=:sfIfStatus";
		Long count = entityManager.createQuery(jpql, Long.class).setParameter("shop", shop).setParameter("sfIfStatus", sfIfStatus).getSingleResult();
		return count > 0;
	}

	@Override
	public InventoryForm findByInventoryCode(String inventoryCode, Member member) {
		 try {
	        String jpql = "select inventoryForm from InventoryForm inventoryForm where inventoryForm.inventoryCode = :inventoryCode and inventoryForm.member =:member";
	        return entityManager.createQuery(jpql, InventoryForm.class).setParameter("inventoryCode", inventoryCode).setParameter("member", member).getSingleResult();
	     } catch (NoResultException e) {
	        return null;
	     }
	}

}
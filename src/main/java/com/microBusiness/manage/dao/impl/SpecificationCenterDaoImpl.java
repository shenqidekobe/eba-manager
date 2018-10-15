package com.microBusiness.manage.dao.impl;

import java.util.List;

import com.microBusiness.manage.dao.SpecificationCenterDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.SpecificationCenter;
import org.springframework.stereotype.Repository;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午8:25
 * Describe:
 * Update:
 */
@Repository
public class SpecificationCenterDaoImpl extends BaseDaoImpl<SpecificationCenter , Long> implements SpecificationCenterDao {

	@Override
	public List<SpecificationCenter> findByName(String name, CategoryCenter categoryCenter) {
		try {
			String jpql = "select specification from SpecificationCenter specification where specification.name =:name and specification.categoryCenter =:categoryCenter";
			return entityManager.createQuery(jpql, SpecificationCenter.class).setParameter("name", name).setParameter("categoryCenter", categoryCenter).getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}

package com.microBusiness.manage.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.microBusiness.manage.dao.SpecificationCenterImportDao;
import com.microBusiness.manage.entity.CategoryCenterImport;
import com.microBusiness.manage.entity.SpecificationCenterImport;

import org.springframework.stereotype.Repository;

@Repository
public class SpecificationCenterImportDaoImpl extends BaseDaoImpl<SpecificationCenterImport , Long> implements SpecificationCenterImportDao {

	@Override
	public List<SpecificationCenterImport> findByName(String name, CategoryCenterImport categoryCenterImport) {
		try {
			String jpql = "select specificationCenterImport from SpecificationCenterImport specificationCenterImport where specificationCenterImport.name =:name and specificationCenterImport.productCategoryImport =:productCategoryImport";
			return entityManager.createQuery(jpql, SpecificationCenterImport.class).setParameter("name", name).setParameter("productCategoryImport", categoryCenterImport).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void delete(String name, CategoryCenterImport categoryCenterImport) {
		String sql=" delete from xx_specification_center_import where name =:name and product_category_import =:productCategoryImport ";
		Query query = entityManager.createNativeQuery(sql.toString());
		query.setParameter("name", name);
		query.setParameter("productCategoryImport", categoryCenterImport.getId());
		query.executeUpdate();
	}

}

package com.microBusiness.manage.dao.impl;

import com.microBusiness.manage.dao.CategoryCenterImportDao;
import com.microBusiness.manage.entity.CategoryCenterImport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 下午8:39
 * Describe:
 * Update:
 */
@Repository
public class CategoryCenterImportDaoImpl extends BaseDaoImpl<CategoryCenterImport , Long> implements CategoryCenterImportDao {

    @Override
    public List<CategoryCenterImport> findByParent(CategoryCenterImport parent, String name) {
        try {
            if (parent != null) {
                String jpql = "select productCategory from CategoryCenterImport productCategory where productCategory.parent =:parent and productCategory.name =:name";
                return entityManager.createQuery(jpql, CategoryCenterImport.class).setParameter("parent", parent).setParameter("name", name).getResultList();
            } else {
                String jpql = "select productCategory from CategoryCenterImport productCategory where productCategory.parent is null and productCategory.name =:name";
                return entityManager.createQuery(jpql, CategoryCenterImport.class).setParameter("name", name).getResultList();
            }

        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
}

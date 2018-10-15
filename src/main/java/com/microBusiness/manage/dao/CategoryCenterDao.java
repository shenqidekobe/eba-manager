package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.ProductCategory;

import java.util.List;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午4:51
 * Describe:
 * Update:
 */
public interface CategoryCenterDao extends BaseDao<CategoryCenter, Long> {
    
	List<CategoryCenter> findByParent(CategoryCenter parent, String name);
	
	List<CategoryCenter> findRoots(Integer count);

    List<CategoryCenter> findParents(CategoryCenter categoryCenter, boolean recursive, Integer count);

    List<CategoryCenter> findChildren(CategoryCenter categoryCenter, boolean recursive, Integer count);

    List<CategoryCenter> findChildren(CategoryCenter CategoryCenter, boolean recursive, Integer count, String searchName);
}

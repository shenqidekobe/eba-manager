package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.CategoryCenter;

import java.util.List;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午3:19
 * Describe:
 * Update:
 */
public interface CategoryCenterService extends BaseService<CategoryCenter , Long> {

    List<CategoryCenter> findRoots();

    List<CategoryCenter> findRoots(Integer count);

    List<CategoryCenter> findRoots(Integer count, boolean useCache);

    List<CategoryCenter> findParents(CategoryCenter categoryCenter, boolean recursive, Integer count);

    List<CategoryCenter> findParents(Long categoryCenterId, boolean recursive, Integer count, boolean useCache);

    List<CategoryCenter> findTree(String categoryName);

    List<CategoryCenter> findTree();

    List<CategoryCenter> findChildren(CategoryCenter categoryCenter, boolean recursive, Integer count);

    

}

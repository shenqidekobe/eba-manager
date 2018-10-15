package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.CategoryCenterImport;

import java.util.List;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 下午8:38
 * Describe:
 * Update:
 */
public interface CategoryCenterImportDao extends BaseDao<CategoryCenterImport , Long> {
    List<CategoryCenterImport> findByParent(CategoryCenterImport parent, String name);
}

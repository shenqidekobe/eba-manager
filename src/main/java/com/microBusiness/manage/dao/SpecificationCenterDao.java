package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.SpecificationCenter;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午8:25
 * Describe:
 * Update:
 */
public interface SpecificationCenterDao extends BaseDao<SpecificationCenter , Long> {
	List<SpecificationCenter> findByName(String name, CategoryCenter categoryCenter);
}

package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.Category;

/**
 * 类别Service
 * 
 * @author 吴战波
 *
 */
public interface CategoryService extends BaseService<Category, Long> {

	/**
	 * 获取类别列表
	 * 
	 * @return 类别列表
	 */
	public List<Category> getCategoryList();


	List<Category> findRoots();

	List<Category> findRoots(Integer count);

	List<Category> findRoots(Integer count, boolean useCache);

	List<Category> findParents(Category category, boolean recursive, Integer count);

	List<Category> findParents(Long categoryId, boolean recursive, Integer count, boolean useCache);

	List<Category> findTree();

	List<Category> findChildren(Category category, boolean recursive, Integer count);

	List<Category> findChildren(Long categoryId, boolean recursive, Integer count, boolean useCache);

	List<Category> findTree(String categoryName);

}

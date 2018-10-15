package com.microBusiness.manage.dao;

import java.util.List;

import com.microBusiness.manage.entity.Category;

/**
 * 类别Dao
 * 
 * @author 吴战波
 *
 */
public interface CategoryDao extends BaseDao<Category, Long>{

	/**
	 * 查询
	 * 
	 * @return 类别列表
	 */
	public List<Category> query();

	/**
	 * 根据商流分类父类ID获取分类列表
	 * 
	 * @param parent
	 * 			商流分类父类ID
	 * @return 分类列表
	 */
	public List<Category> queryByParent(Category parent);


	List<Category> findRoots(Integer count);

	List<Category> findParents(Category category, boolean recursive, Integer count);

	List<Category> findChildren(Category category, boolean recursive, Integer count);

	List<Category> findChildren(Category category , boolean recursive , Integer count , String categoryName) ;

}

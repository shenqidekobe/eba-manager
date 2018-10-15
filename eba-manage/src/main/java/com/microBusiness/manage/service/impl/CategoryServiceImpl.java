package com.microBusiness.manage.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.microBusiness.manage.dao.CategoryDao;
import com.microBusiness.manage.entity.Category;
import com.microBusiness.manage.service.CategoryService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 类别Service实现
 * 
 * @author 吴战波
 *
 */
@Service("CategoryServiceImpl")
public class CategoryServiceImpl extends BaseServiceImpl<Category, Long> implements CategoryService{

	@Resource(name = "categoryDaoImpl")
	private CategoryDao categoryDao;
	
	@Override
	public List<Category> getCategoryList() {
		// 获取所有类别
		List<Category> categorysOld = categoryDao.findRoots(null);
		
		/*// 根据parent分类
		for (Category categoryOld : categorysOld) {
			List<Category> categorysNew = categoryDao.queryByParent(categoryOld);
			categoryOld.setCategorys(categorysNew);
			for (Category category : categorysNew) {
				List<Category> categorys = categoryDao.queryByParent(category.getId());
				category.setCategorys(categorys);
			}
		}*/

		return categorysOld;
	}


	@Transactional(readOnly = true)
	public List<Category> findRoots() {
		return categoryDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<Category> findRoots(Integer count) {
		return categoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<Category> findRoots(Integer count, boolean useCache) {
		return categoryDao.findRoots(count);
	}

	@Transactional(readOnly = true)
	public List<Category> findParents(Category category, boolean recursive, Integer count) {
		return categoryDao.findParents(category, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<Category> findParents(Long categoryId, boolean recursive, Integer count, boolean useCache) {
		Category category = categoryDao.find(categoryId);
		if (categoryId != null && category == null) {
			return Collections.emptyList();
		}
		return categoryDao.findParents(category, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<Category> findTree() {
		return categoryDao.findChildren(null, true, null);
	}

	@Transactional(readOnly = true)
	public List<Category> findChildren(Category category, boolean recursive, Integer count) {
		return categoryDao.findChildren(category, recursive, count);
	}

	@Transactional(readOnly = true)
	public List<Category> findChildren(Long categoryId, boolean recursive, Integer count, boolean useCache) {
		Category category = categoryDao.find(categoryId);
		if (categoryId != null && category == null) {
			return Collections.emptyList();
		}
		return categoryDao.findChildren(category, recursive, count);
	}

	@Override
	@Transactional
	public Category save(Category category) {
		Assert.notNull(category);

		setValue(category);
		return super.save(category);
	}

	@Override
	@Transactional
	public Category update(Category category) {
		Assert.notNull(category);

		setValue(category);
		for (Category children : categoryDao.findChildren(category, true, null)) {
			setValue(children);
		}
		return super.update(category);
	}

	@Override
	@Transactional
	public Category update(Category category, String... ignoreProperties) {
		return super.update(category, ignoreProperties);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	public void delete(Category category) {
		super.delete(category);
	}

	private void setValue(Category category) {
		if (category == null) {
			return;
		}
		Category parent = category.getParent();
		if (parent != null) {
			category.setTreePath(parent.getTreePath() + parent.getId() + Category.TREE_PATH_SEPARATOR);
		} else {
			category.setTreePath(Category.TREE_PATH_SEPARATOR);
		}
		category.setGrade(category.getParentIds().length);
	}


	@Override
	public List<Category> findTree(String categoryName) {
		return categoryDao.findChildren(null, true, null , categoryName);
	}
}

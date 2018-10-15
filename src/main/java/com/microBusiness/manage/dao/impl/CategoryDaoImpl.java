package com.microBusiness.manage.dao.impl;

import java.util.*;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import com.microBusiness.manage.dao.CategoryDao;
import com.microBusiness.manage.entity.Category;

/**
 * 类别Dao实现
 * 
 * @author 吴战波
 *
 */
@Repository("categoryDaoImpl")
public class CategoryDaoImpl extends BaseDaoImpl<Category, Long> implements CategoryDao{

	@Override
	public List<Category> query() {
		try {
			String jpql = "select category from Category category where category.parent is null order by category.order";
			return entityManager.createQuery(jpql, Category.class).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Category> queryByParent(Category parent) {
		try {
			String jpql = "select category from Category category where category.parent =:parent order by category.order";
			return entityManager.createQuery(jpql, Category.class).setParameter("parent", parent).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Category> findRoots(Integer count) {
		String jpql = "select category from Category category where category.parent is null order by category.order asc";
		TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public List<Category> findParents(Category category, boolean recursive, Integer count) {
		if (category == null || category.getParent() == null) {
			return Collections.emptyList();
		}
		TypedQuery<Category> query;
		if (recursive) {
			String jpql = "select category from Category category where category.id in (:ids) order by category.grade asc";
			query = entityManager.createQuery(jpql, Category.class).setParameter("ids", Arrays.asList(category.getParentIds()));
		} else {
			String jpql = "select category from Category category where category = :category";
			query = entityManager.createQuery(jpql, Category.class).setParameter("category", category.getParent());
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	/**
	 *
	 * @param category
	 * @param recursive 是否递归
	 * @param count
	 * @return
	 */
	public List<Category> findChildren(Category category, boolean recursive, Integer count) {
		TypedQuery<Category> query;
		if (recursive) {
			if (category != null) {
				String jpql = "select category from Category category where category.treePath like :treePath order by category.grade asc, category.order asc";
				query = entityManager.createQuery(jpql, Category.class).setParameter("treePath", "%" + Category.TREE_PATH_SEPARATOR + category.getId() + Category.TREE_PATH_SEPARATOR + "%");
			} else {
				String jpql = "select category from Category category order by category.grade asc, category.order asc";
				query = entityManager.createQuery(jpql, Category.class);
			}
			if (count != null) {
				query.setMaxResults(count);
			}
			List<Category> result = query.getResultList();
			sort(result);
			return result;
		} else {
			String jpql = "select category from Category category where category.parent = :parent order by category.order asc";
			query = entityManager.createQuery(jpql, Category.class).setParameter("parent", category);
			if (count != null) {
				query.setMaxResults(count);
			}
			return query.getResultList();
		}
	}

	private void sort(List<Category> categories) {
		if (CollectionUtils.isEmpty(categories)) {
			return;
		}
		final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
		for (Category category : categories) {
			orderMap.put(category.getId(), category.getOrder());
		}
		Collections.sort(categories, new Comparator<Category>() {
			@Override
			public int compare(Category category1, Category category2) {
				Long[] ids1 = (Long[]) ArrayUtils.add(category1.getParentIds(), category1.getId());
				Long[] ids2 = (Long[]) ArrayUtils.add(category2.getParentIds(), category2.getId());
				Iterator<Long> iterator1 = Arrays.asList(ids1).iterator();
				Iterator<Long> iterator2 = Arrays.asList(ids2).iterator();
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				while (iterator1.hasNext() && iterator2.hasNext()) {
					Long id1 = iterator1.next();
					Long id2 = iterator2.next();
					Integer order1 = orderMap.get(id1);
					Integer order2 = orderMap.get(id2);
					compareToBuilder.append(order1, order2).append(id1, id2);
					if (!iterator1.hasNext() || !iterator2.hasNext()) {
						compareToBuilder.append(category1.getGrade(), category2.getGrade());
					}
				}
				return compareToBuilder.toComparison();
			}
		});
	}


	@Override
	public List<Category> findChildren(Category category, boolean recursive, Integer count, String categoryName) {
		TypedQuery<Category> query;
		StringBuffer jpqlSb = new StringBuffer("select category from Category category where 1=1");
		if(StringUtils.isNotEmpty(categoryName)){
			jpqlSb.append(" and category.name like :categoryName");
		}

		jpqlSb.append(" order by category.grade asc, category.order asc");

		query = entityManager.createQuery(jpqlSb.toString(), Category.class);
		if(StringUtils.isNotEmpty(categoryName)){
			query.setParameter("categoryName", "%" + categoryName + "%") ;
		}

		if (count != null) {
			query.setMaxResults(count);
		}
		List<Category> result = query.getResultList();
		sort(result);
		return result;
	}
}

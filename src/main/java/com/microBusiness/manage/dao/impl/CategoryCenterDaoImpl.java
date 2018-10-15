package com.microBusiness.manage.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import com.microBusiness.manage.dao.CategoryCenterDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.entity.ProductCategory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午4:52
 * Describe:
 * Update:
 */
@Repository("categoryCenterDaoImpl")
public class CategoryCenterDaoImpl extends BaseDaoImpl<CategoryCenter, Long> implements CategoryCenterDao {

    @Override
    public List<CategoryCenter> findRoots(Integer count) {
        return null;
    }

    @Override
    public List<CategoryCenter> findParents(CategoryCenter categoryCenter, boolean recursive, Integer count) {
        return null;
    }

    @Override
    public List<CategoryCenter> findChildren(CategoryCenter categoryCenter, boolean recursive, Integer count) {
        return this.findChildren(categoryCenter , recursive , count , null);
    }

    @Override
    public List<CategoryCenter> findChildren(CategoryCenter categoryCenter, boolean recursive, Integer count, String searchName) {

        TypedQuery<CategoryCenter> query;
        StringBuffer queryStr = new StringBuffer();
        if (recursive) {
            if (categoryCenter != null) {
                queryStr.append("select categoryCenter from CategoryCenter categoryCenter where categoryCenter.treePath like :treePath");

                if (null != searchName) {
                    queryStr.append(" and categoryCenter.name like :searchName");
                }

                queryStr.append(" order by categoryCenter.grade asc, categoryCenter.order asc");

                query = entityManager.createQuery(queryStr.toString(), CategoryCenter.class).setParameter("treePath", "%" + CategoryCenter.TREE_PATH_SEPARATOR + categoryCenter.getId() + CategoryCenter.TREE_PATH_SEPARATOR + "%");

                if (null != searchName) {
                    query.setParameter("searchName", "%" + searchName + "%");
                }

            } else {
                queryStr.append("select categoryCenter from CategoryCenter categoryCenter where 1=1");

                if (null != searchName) {
                    queryStr.append(" and categoryCenter.name like :searchName");
                }

                queryStr.append(" order by categoryCenter.grade asc, categoryCenter.order asc");

                query = entityManager.createQuery(queryStr.toString(), CategoryCenter.class);

                if (null != searchName) {
                    query.setParameter("searchName", "%" + searchName + "%");
                }

            }
            if (count != null) {
                query.setMaxResults(count);
            }
            List<CategoryCenter> result = query.getResultList();
            sort(result);
            return result;
        } else {

            queryStr.append("select categoryCenter from CategoryCenter categoryCenter where 1=1 and categoryCenter.parent = :parent");

            if (null != searchName) {
                queryStr.append(" and categoryCenter.name like :searchName");
            }

            queryStr.append(" order by categoryCenter.grade asc, categoryCenter.order asc");


            query = entityManager.createQuery(queryStr.toString(), CategoryCenter.class).setParameter("parent", categoryCenter);


            if (null != searchName) {
                query.setParameter("searchName", "%" + searchName + "%");
            }

            if (count != null) {
                query.setMaxResults(count);
            }
            return query.getResultList();
        }
    }


    private void sort(List<CategoryCenter> productCategories) {
        if (CollectionUtils.isEmpty(productCategories)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
        for (CategoryCenter categoryCenter : productCategories) {
            orderMap.put(categoryCenter.getId(), categoryCenter.getOrder());
        }
        Collections.sort(productCategories, new Comparator<CategoryCenter>() {
            @Override
            public int compare(CategoryCenter categoryCenter1, CategoryCenter categoryCenter2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(categoryCenter1.getParentIds(), categoryCenter1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(categoryCenter2.getParentIds(), categoryCenter2.getId());
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
                        compareToBuilder.append(categoryCenter1.getGrade(), categoryCenter2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }

    @Override
	public List<CategoryCenter> findByParent(CategoryCenter parent, String name) {
		try {
			if (parent != null) {
				String jpql = "select productCategory from CategoryCenter productCategory where productCategory.parent =:parent and productCategory.name =:name";
				return entityManager.createQuery(jpql, CategoryCenter.class).setParameter("parent", parent).setParameter("name", name).getResultList();
			} else {
				String jpql = "select productCategory from CategoryCenter productCategory where productCategory.parent is null and productCategory.name =:name";
				return entityManager.createQuery(jpql, CategoryCenter.class).setParameter("name", name).getResultList();
			}
			
		} catch (Exception e) {
			return null;
		}
	}

}

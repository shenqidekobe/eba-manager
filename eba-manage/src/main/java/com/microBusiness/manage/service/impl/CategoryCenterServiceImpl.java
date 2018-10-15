package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.dao.CategoryCenterDao;
import com.microBusiness.manage.entity.CategoryCenter;
import com.microBusiness.manage.service.CategoryCenterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/5 下午3:24
 * Describe:
 * Update:
 */
@Service
public class CategoryCenterServiceImpl extends BaseServiceImpl<CategoryCenter, Long> implements CategoryCenterService {

    @Resource
    private CategoryCenterDao categoryCenterDao ;

    @Override
    public List<CategoryCenter> findRoots() {
        return null;
    }

    @Override
    public List<CategoryCenter> findRoots(Integer count) {
        return null;
    }

    @Override
    public List<CategoryCenter> findRoots(Integer count, boolean useCache) {
        return null;
    }

    @Override
    public List<CategoryCenter> findParents(CategoryCenter categoryCenter, boolean recursive, Integer count) {
        return null;
    }

    @Override
    public List<CategoryCenter> findParents(Long categoryCenterId, boolean recursive, Integer count, boolean useCache) {
        return null;
    }

    @Override
    public List<CategoryCenter> findTree(String categoryName) {
        return categoryCenterDao.findChildren(null, true, null , categoryName);
    }

    @Override
    public List<CategoryCenter> findChildren(CategoryCenter categoryCenter, boolean recursive, Integer count) {
        return categoryCenterDao.findChildren(categoryCenter, true, null , null);
    }

    private void setValue(CategoryCenter productCategory) {
        if (productCategory == null) {
            return;
        }
        CategoryCenter parent = productCategory.getParent();
        if (parent != null) {
            productCategory.setTreePath(parent.getTreePath() + parent.getId() + CategoryCenter.TREE_PATH_SEPARATOR);
        } else {
            productCategory.setTreePath(CategoryCenter.TREE_PATH_SEPARATOR);
        }
        productCategory.setGrade(productCategory.getParentIds().length);
    }


    @Override
    public CategoryCenter save(CategoryCenter entity) {

        Assert.notNull(entity);

        setValue(entity);

        return super.save(entity);
    }

    @Transactional
    @Override
    public CategoryCenter update(CategoryCenter entity, String... ignoreProperties) {
        return super.update(entity, ignoreProperties);
    }

    @Override
    @Transactional
    public CategoryCenter update(CategoryCenter productCategory) {
        Assert.notNull(productCategory);

        setValue(productCategory);
        for (CategoryCenter children : categoryCenterDao.findChildren(productCategory, true, null)) {
            setValue(children);
        }
        return super.update(productCategory);
    }

    @Override
    public List<CategoryCenter> findTree() {
        return this.findTree(null);
    }
}

package com.microBusiness.manage.service.impl;

import com.microBusiness.manage.dao.DepartmentDao;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.service.BaseService;
import com.microBusiness.manage.service.DepartmentService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by mingbai on 2017/8/24.
 * 功能描述：
 * 修改记录：
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, Long> implements DepartmentService {

    @Resource
    private DepartmentDao departmentDao ;

    @Override
    public List<Department> findRoots(Integer count, Supplier supplier) {
        return departmentDao.findRoots(count, supplier);
    }

    @Override
    public List<Department> findTree(Supplier supplier, String searchName) {
        return departmentDao.findChildren(null, true, null , supplier , searchName);
    }

    @Override
    @Transactional
    public Department save(Department department) {
        Assert.notNull(department);
        setValue(department);
        return super.save(department);
    }

    private void setValue(Department department) {
        if (department == null) {
            return;
        }
        Department parent = department.getParent();
        if (parent != null) {
            department.setTreePath(parent.getTreePath() + parent.getId() + Department.TREE_PATH_SEPARATOR);
        } else {
            department.setTreePath(Department.TREE_PATH_SEPARATOR);
        }
        department.setGrade(department.getParentIds().length);
    }

    @Override
    public List<Department> findChildren(Department department, boolean recursive, Integer count) {
        return departmentDao.findChildren(department, recursive, count);
    }

    public Department update(Department department) {
        Assert.notNull(department);

        setValue(department);
        for (Department children : departmentDao.findChildren(department, true, null)) {
            setValue(children);
        }
        return super.update(department);
    }

    @Override
    @Transactional
    public Department update(Department department, String... ignoreProperties) {
        return super.update(department, ignoreProperties);
    }

}

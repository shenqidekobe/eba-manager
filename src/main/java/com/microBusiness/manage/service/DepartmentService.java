package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Supplier;

import java.util.List;

/**
 * Created by mingbai on 2017/8/24.
 * 功能描述：
 * 修改记录：
 */
public interface DepartmentService extends BaseService<Department, Long> {

    List<Department> findRoots(Integer count, Supplier supplier);

    List<Department> findTree(Supplier supplier , String searchName);

    List<Department> findChildren(Department department, boolean recursive, Integer count);
}

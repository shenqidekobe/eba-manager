package com.microBusiness.manage.dao;

import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Supplier;

import java.util.List;

/**
 * Created by mingbai on 2017/8/24.
 * 功能描述：
 * 修改记录：
 */
public interface DepartmentDao extends BaseDao<Department, Long>{
    List<Department> findRoots(Integer count , Supplier supplier);

    List<Department> findParents(Department department, boolean recursive, Integer count , Supplier supplier);

    List<Department> findChildren(Department department, boolean recursive, Integer count, Supplier supplier , String searchName);

    List<Department> findChildren(Department department, boolean recursive, Integer count);


}

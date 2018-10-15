package com.microBusiness.manage.dao;

import com.microBusiness.manage.dao.impl.BaseDaoImpl;
import com.microBusiness.manage.entity.Department;
import com.microBusiness.manage.entity.Supplier;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by mingbai on 2017/8/24.
 * 功能描述：
 * 修改记录：
 */
@Repository
public class DepartmentDaoImpl extends BaseDaoImpl<Department, Long> implements DepartmentDao {

    @Override
    public List<Department> findRoots(Integer count, Supplier supplier) {
        String jpql = "select department from Department department where department.parent is null and department.supplier=:supplier order by department.order asc";
        TypedQuery<Department> query = entityManager.createQuery(jpql, Department.class).setParameter("supplier", supplier);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    @Override
    public List<Department> findParents(Department department, boolean recursive, Integer count, Supplier supplier) {
        return null;
    }

    @Override
    public List<Department> findChildren(Department department, boolean recursive, Integer count, Supplier supplier, String searchName) {

        TypedQuery<Department> query;
        StringBuffer queryStr = new StringBuffer() ;
        if (recursive) {
            if (department != null) {
                queryStr.append("select department from Department department where department.treePath like :treePath") ;
                if(null != supplier){
                    queryStr.append(" and supplier=:supplier");
                }
                if(null != searchName) {
                    queryStr.append(" and department.name like :searchName");
                }

                queryStr.append(" order by department.grade asc, department.order asc");

                query = entityManager.createQuery(queryStr.toString(), Department.class).setParameter("treePath", "%" + Department.TREE_PATH_SEPARATOR + department.getId() + Department.TREE_PATH_SEPARATOR + "%");

                if(null != supplier){
                    query.setParameter("supplier" , supplier);
                }
                if(null != searchName) {
                    query.setParameter("searchName" , "%" + searchName + "%");
                }

            } else {

                queryStr.append("select department from Department department where 1=1") ;

                if(null != supplier){
                    queryStr.append(" and supplier=:supplier");
                }
                if(null != searchName) {
                    queryStr.append(" and department.name like :searchName");
                }

                queryStr.append(" order by department.grade asc, department.order asc");

                query = entityManager.createQuery(queryStr.toString() , Department.class);

                if(null != supplier){
                    query.setParameter("supplier" , supplier);
                }
                if(null != searchName) {
                    query.setParameter("searchName" , "%" + searchName + "%");
                }

            }
            if (count != null) {
                query.setMaxResults(count);
            }
            List<Department> result = query.getResultList();
            sort(result);
            return result;
        } else {

            queryStr.append("select department from Department department where 1=1 and department.parent = :parent") ;

            if(null != supplier){
                queryStr.append(" and supplier=:supplier");
            }
            if(null != searchName) {
                queryStr.append(" and department.name like :searchName");
            }

            queryStr.append(" order by department.grade asc, department.order asc");


            query = entityManager.createQuery(queryStr.toString(), Department.class).setParameter("parent", department);

            if(null != supplier){
                query.setParameter("supplier" , supplier);
            }
            if(null != searchName) {
                query.setParameter("searchName" , "%" + searchName + "%");
            }

            if (count != null) {
                query.setMaxResults(count);
            }
            return query.getResultList();
        }
    }


    private void sort(List<Department> departments) {
        if (CollectionUtils.isEmpty(departments)) {
            return;
        }
        final Map<Long, Integer> orderMap = new HashMap<Long, Integer>();
        for (Department department : departments) {
            orderMap.put(department.getId(), department.getOrder());
        }
        Collections.sort(departments, new Comparator<Department>() {
            @Override
            public int compare(Department department1, Department department2) {
                Long[] ids1 = (Long[]) ArrayUtils.add(department1.getParentIds(), department1.getId());
                Long[] ids2 = (Long[]) ArrayUtils.add(department2.getParentIds(), department2.getId());
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
                        compareToBuilder.append(department1.getGrade(), department2.getGrade());
                    }
                }
                return compareToBuilder.toComparison();
            }
        });
    }


    @Override
    public List<Department> findChildren(Department department, boolean recursive, Integer count) {
        TypedQuery<Department> query;
        if (recursive) {
            if (department != null) {
                String jpql = "select department from Department department where department.treePath like :treePath order by department.grade asc, department.order asc";
                query = entityManager.createQuery(jpql, Department.class).setParameter("treePath", "%" + Department.TREE_PATH_SEPARATOR + department.getId() + Department.TREE_PATH_SEPARATOR + "%");
            } else {
                String jpql = "select department from Department department order by department.grade asc, department.order asc";
                query = entityManager.createQuery(jpql, Department.class);
            }
            if (count != null) {
                query.setMaxResults(count);
            }
            List<Department> result = query.getResultList();
            sort(result);
            return result;
        } else {
            String jpql = "select department from Department department where department.parent = :parent order by department.order asc";
            query = entityManager.createQuery(jpql, Department.class).setParameter("parent", department);
            if (count != null) {
                query.setMaxResults(count);
            }
            return query.getResultList();
        }
    }
}

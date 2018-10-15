package com.microBusiness.manage.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mingbai on 2017/8/24.
 * 功能描述：部门
 * 修改记录：
 */
@Entity
@Table(name = "t_department")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_department")
public class Department extends OrderEntity<Long> {

    private static final long serialVersionUID = -1641203197283250519L;

    public static final String TREE_PATH_SEPARATOR = ",";

    private String name;

    private String treePath;

    private Integer grade;

    private Department parent;

    private Set<Department> children = new HashSet<Department>();

    private Supplier supplier ;

    private Set<Admin> admins = new HashSet<>();


    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public String getTreePath() {
        return treePath;
    }

    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    @Column(nullable = false)
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("order asc")
    public Set<Department> getChildren() {
        return children;
    }

    public void setChildren(Set<Department> children) {
        this.children = children;
    }


    @Transient
    public Long[] getParentIds() {
        String[] parentIds = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
        Long[] result = new Long[parentIds.length];
        for (int i = 0; i < parentIds.length; i++) {
            result[i] = Long.valueOf(parentIds[i]);
        }
        return result;
    }

    @Transient
    public List<Department> getParents() {
        List<Department> parents = new ArrayList<Department>();
        recursiveParents(parents, this);
        return parents;
    }

    private void recursiveParents(List<Department> parents, Department department) {
        if (department == null) {
            return;
        }
        Department parent = department.getParent();
        if (parent != null) {
            parents.add(0, parent);
            recursiveParents(parents, parent);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @OneToMany(mappedBy = "department" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
    }
}

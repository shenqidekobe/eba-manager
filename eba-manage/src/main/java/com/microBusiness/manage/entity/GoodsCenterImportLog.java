package com.microBusiness.manage.entity;

import javax.persistence.*;

import java.util.Set;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 上午11:39
 * Describe: 导入日志
 * Update:
 */
@Entity
@Table(name = "t_gc_import_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_gc_import_log")
public class GoodsCenterImportLog extends BaseEntity<Long>{

	private static final long serialVersionUID = -5550319692579970110L;

	private Set<GoodsCenterImportInfo> infos ;

    private Admin admin ;

    private int total ;

    private int successNum ;

    private int errorNum ;

    @OneToMany(mappedBy = "log" , fetch = FetchType.LAZY)
    public Set<GoodsCenterImportInfo> getInfos() {
        return infos;
    }

    public void setInfos(Set<GoodsCenterImportInfo> infos) {
        this.infos = infos;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "null"))
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }
}

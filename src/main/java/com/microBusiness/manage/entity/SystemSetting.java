package com.microBusiness.manage.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by mingbai on 2017/10/10.
 * 功能描述：系统设置
 * 修改记录：
 */
@Entity
@Table(name = "t_system_setting")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_system_setting")
public class SystemSetting extends BaseEntity<Long>  {

    private static final long serialVersionUID = -6818047420690062917L;

    private Supplier supplier ;

    private Boolean isDistributionModel ;


    @OneToOne(fetch = FetchType.LAZY)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @NotNull
    @Column(nullable = false)
    public Boolean getIsDistributionModel() {
        return isDistributionModel;
    }

    public void setIsDistributionModel(Boolean isDistributionModel) {
        this.isDistributionModel = isDistributionModel;
    }

    @PrePersist
    public void prePersist() {
        if(null == this.getIsDistributionModel()){
            this.setIsDistributionModel(Boolean.FALSE);
        }
    }
}

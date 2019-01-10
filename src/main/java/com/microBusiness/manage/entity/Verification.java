package com.microBusiness.manage.entity;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Indexed
@Entity
@Table(name = "t_verification")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_verification")
public class Verification extends BaseEntity<Long> {


    private String tag;//标识

    private String batchNo; //批次

    private Date proofTime;


    public void setProofTime(Date proofTime) {
        this.proofTime = proofTime;
    }

    public Date getProofTime() {
        return proofTime;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    @NotEmpty
    public String getTag() {
        return tag;
    }

    @NotEmpty
    public String getBatchNo() {
        return batchNo;
    }
}

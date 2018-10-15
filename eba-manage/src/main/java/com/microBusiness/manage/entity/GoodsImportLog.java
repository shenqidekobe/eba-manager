package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "xx_goods_import_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods_import_log")
public class GoodsImportLog extends BaseEntity<Long> {

	private static final long serialVersionUID = -521951214003475595L;

	private List<GoodsImportInfo> goodsImportInfos = new ArrayList<>();
    /**
     * 操作人
     */
    private Admin admin ;

    private int total ;

    private int successNum ;

    private int errorNum ;

    @OneToMany(mappedBy = "goodsImportLog",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<GoodsImportInfo> getGoodsImportInfos() {
		return goodsImportInfos;
	}

	public void setGoodsImportInfos(List<GoodsImportInfo> goodsImportInfos) {
		this.goodsImportInfos = goodsImportInfos;
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

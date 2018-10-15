package com.microBusiness.manage.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：
 * 修改记录：
 */
@Entity
@Table(name = "t_need_import_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need_import_log")
public class NeedImportLog extends BaseEntity<Long> {


    private static final long serialVersionUID = 1757204715308457721L;

    private List<NeedImportInfo> needImportInfos = new ArrayList<>();
    /**
     * 操作人
     */
    private Admin admin ;

    private int total ;

    //店铺类型
   	private ShopType shopType;
    
    private int successNum ;

    private int errorNum ;

    @OneToMany(mappedBy = "needImportLog", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<NeedImportInfo> getNeedImportInfos() {
        return needImportInfos;
    }

    public void setNeedImportInfos(List<NeedImportInfo> needImportInfos) {
        this.needImportInfos = needImportInfos;
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

	public ShopType getShopType() {
		return shopType;
	}

	public void setShopType(ShopType shopType) {
		this.shopType = shopType;
	}
    
}

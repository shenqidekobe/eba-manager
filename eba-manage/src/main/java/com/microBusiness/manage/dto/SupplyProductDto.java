package com.microBusiness.manage.dto;

import com.microBusiness.manage.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by mingbai on 2017/7/11.
 * 功能描述：
 * 修改记录：
 */
public class SupplyProductDto implements Serializable {

    private static final long serialVersionUID = -6235242614451475734L;

    private Product product ;
    private Integer minOrderQuantity ;
    private BigDecimal supplyPrice;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

	public BigDecimal getSupplyPrice() {
		return supplyPrice;
	}

	public void setSupplyPrice(BigDecimal supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
    
}

package com.microBusiness.manage.entity;

import com.microBusiness.manage.controller.admin.OwnOrderController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingbai on 2017/4/14.
 * 功能描述：
 * 修改记录：
 */
public class OrderProductForm {

    List<OwnOrderItem> ownOrderItems = new ArrayList<>();

    public List<OwnOrderItem> getOwnOrderItems() {
        return ownOrderItems;
    }

    public void setOwnOrderItems(List<OwnOrderItem> ownOrderItems) {
        this.ownOrderItems = ownOrderItems;
    }

    public static class OwnOrderItem {
        private Long productId;
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}

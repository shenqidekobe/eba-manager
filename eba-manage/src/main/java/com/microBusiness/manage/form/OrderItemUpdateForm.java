package com.microBusiness.manage.form;

import com.microBusiness.manage.entity.OrderItem;

import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/5/31.
 * 功能描述：订单item修改表单实体类
 * 修改记录：
 */
public class OrderItemUpdateForm {

    private Date reDate;

    private List<OrderItem> orderItems;

    public Date getReDate() {
        return reDate;
    }

    public void setReDate(Date reDate) {
        this.reDate = reDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}

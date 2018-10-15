package com.microBusiness.manage.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mingbai on 2017/4/19.
 * 功能描述：用于多地址下单中的多地址集合
 * 修改记录：
 */
public class OrderNeedsForm {
    private List<OrderNeedsItem> orderNeedsItems = new ArrayList<>();

    public List<OrderNeedsItem> getOrderNeedsItems() {
        return orderNeedsItems;
    }

    public void setOrderNeedsItems(List<OrderNeedsItem> orderNeedsItems) {
        this.orderNeedsItems = orderNeedsItems;
    }

    public static class OrderNeedsItem{
        private Long needId ;
        private String memo ;
        private Date reDate ;
        private Long areaId;
        private String address ;

        public Long getNeedId() {
            return needId;
        }

        public void setNeedId(Long needId) {
            this.needId = needId;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public Date getReDate() {
            return reDate;
        }

        public void setReDate(Date reDate) {
            this.reDate = reDate;
        }

		public Long getAreaId() {
			return areaId;
		}

		public void setAreaId(Long areaId) {
			this.areaId = areaId;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
        
    }

}

package com.microBusiness.manage.entity.ass;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Need;

/**
 * 助手商品个体客户关系表
 * @author Administrator
 *
 */
@Indexed
@Entity
@Table(name = "ass_goods_need")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_goods_need")
public class AssGoodsNeed extends BaseEntity<Long> {

	private static final long serialVersionUID = 3610156279939728313L;

	private AssProduct assProduct;
	
	private Need need;

	@ManyToOne(fetch = FetchType.LAZY)
	public AssProduct getAssProduct() {
		return assProduct;
	}

	public void setAssProduct(AssProduct assProduct) {
		this.assProduct = assProduct;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Need getNeed() {
		return need;
	}

	public void setNeed(Need need) {
		this.need = need;
	}

}

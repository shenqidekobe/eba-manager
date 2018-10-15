package com.microBusiness.manage.entity.ass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.entity.BaseEntity;

@Entity
@Table(name = "ass_list_log")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_list_log")
public class AssListLog  extends BaseEntity<Long>{

	private static final long serialVersionUID = 8655171463411179347L;
	
	public enum Type{
		initiated, //发起
		participate,//参与
		dropOut, 	//退出
		theEnd, 	//终结
	}
	
	//记录人
	private String name;
	
	//操作类型
	private Type type;
	
	//备注所属清单
	private AssList assList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assList", nullable = false, updatable = false)
	public AssList getAssList() {
		return assList;
	}

	public void setAssList(AssList assList) {
		this.assList = assList;
	}
	
	
}

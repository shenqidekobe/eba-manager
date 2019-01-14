package com.microBusiness.manage.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "xx_dict")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_dict")
public class Dict extends BaseEntity<Long> {

	private static final long serialVersionUID = -1037721305143580361L;
	
	public static final Long DEFAULT_ID=100l;
	
	private String type;
	@Lob
	private String json;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

}

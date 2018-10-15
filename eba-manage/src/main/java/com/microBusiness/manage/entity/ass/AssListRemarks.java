package com.microBusiness.manage.entity.ass;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.RemarksFile;

@Entity
@Table(name = "ass_list_remarks")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_list_remarks")
public class AssListRemarks  extends BaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3282536857973433221L;
	//备注信息
	private String description;
	
	//备注人
	private String name;
	
	//备注附件
	private List<RemarksFile> annex;
	
	//备注人账号
	private AssChildMember assChildMember;
	
	//备注所属清单
	private AssList assList;

	@Column(nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(updatable = false,length = 4000)
	@Convert(converter = RemarksUrlConverter.class)
	public List<RemarksFile> getAnnex() {
		return annex;
	}

	public void setAnnex(List<RemarksFile> annex) {
		this.annex = annex;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assChildMember", nullable = false, updatable = false)
	public AssChildMember getAssChildMember() {
		return assChildMember;
	}

	public void setAssChildMember(AssChildMember assChildMember) {
		this.assChildMember = assChildMember;
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
	
	@Converter
	public static class RemarksUrlConverter extends BaseAttributeConverter<List<RemarksFile>> implements AttributeConverter<Object, String> {
		
	}
	
}

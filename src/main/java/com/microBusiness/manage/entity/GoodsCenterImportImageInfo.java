package com.microBusiness.manage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.microBusiness.manage.BaseAttributeConverter;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "t_goods_center_import_image_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_goods_center_import_image_info")
public class GoodsCenterImportImageInfo extends BaseEntity<Long> {

	private static final long serialVersionUID = 6798409655995577189L;

	private String sn;
	
	private String image;
	
	private List<Second> images = new ArrayList<Second>();
	
    private Supplier supplier;
    
    private String batch;
    
    private String name;
    
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, updatable = false, unique = false)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Field(store = Store.YES, index = Index.NO, analyze = Analyze.NO)
	@Length(max = 200)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	@Valid
	@Column(length = 4000)
	@Convert(converter = ImageConverter.class)
	public List<Second> getImages() {
		return images;
	}

	public void setImages(List<Second> images) {
		this.images = images;
	}

	@Converter
	public static class ImageConverter extends BaseAttributeConverter<List<Second>> implements AttributeConverter<Object, String> {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
	public static class Second implements Serializable {

		private static final long serialVersionUID = -2833297699569163205L;

		private Integer id;

		private String value;

		@NotNull
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@NotEmpty
		@Length(max = 200)
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, updatable = false, unique = false)
	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

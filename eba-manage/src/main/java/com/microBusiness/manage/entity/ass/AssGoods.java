package com.microBusiness.manage.entity.ass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.microBusiness.manage.BaseAttributeConverter;
import com.microBusiness.manage.BigDecimalNumericFieldBridge;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.entity.BaseEntity;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Product;
import com.microBusiness.manage.entity.ProductCategory;
import com.microBusiness.manage.entity.ProductImage;
import com.microBusiness.manage.entity.SpecificationItem;
import com.microBusiness.manage.entity.Supplier;
import com.microBusiness.manage.entity.Goods.ProductImageConverter;
import com.microBusiness.manage.entity.Goods.SpecificationItemConverter;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 助手商品
 * @author yuezhiwei
 *
 */
@Indexed
@Entity
@Table(name = "ass_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_ass_goods")
public class AssGoods extends BaseEntity<Long> {

	private static final long serialVersionUID = 4491788465444420058L;

	private String sn;

	private String name;
	
	//private BigDecimal price;

	private List<String> image;

	//private Boolean isMarketable;

	//private ProductCategory productCategory;
		
	private List<SpecificationItem> specificationItems = new ArrayList<SpecificationItem>();

	private Set<AssProduct> assProducts = new HashSet<AssProduct>();
	
	private List<ProductImage> productImages = new ArrayList<ProductImage>();

	//private Supplier supplier ;
	
	/*public enum OpenButton {
		//全部开放
		allOpen,
		//部分开放
		partiallyOpen
	}
	//针对个体客户是否全部开放
	private OpenButton openButton;*/
	
	private AssCustomerRelation assCustomerRelation;
	
	public enum Unit {
		one,//个
		pieces,//件
		bottle,//瓶
		box,//箱
		bag,//袋
		barrel,//桶
		opies,//份
		frame,//盒
		pack //包
	}
	
	//基本单位
	private Unit unit;
	
	public enum Label {
		//新品
		newProducts,
		//热卖
		selling,
		//促销
		promotions,
		//特惠
		specialOffer,
		//人气
		popularity,
		//爆款
		explosions
	}
	
	//标签
	private List<Label> labels;
	
	//商品详情描述
	private String detailsDescription;
	
	//商品详情图片
	private List<String> detailsImage;
	
	private AssGoods source;
	
	private Goods goods;
	
	public enum SourceType {
		//从后台同步
        syncBackstage,
      //前端之前目录
		MOBILE
	}
	
	private SourceType sourceType;
	
	private String details;

	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, updatable = false, unique = true)
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ImageConverter.class)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	/*@Column(nullable = false)
	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
*/
	@OneToMany(mappedBy = "assGoods", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Where(clause="deleted=0")
	@OrderBy("createDate asc")
	public Set<AssProduct> getAssProducts() {
		return assProducts;
	}

	public void setAssProducts(Set<AssProduct> assProducts) {
		this.assProducts = assProducts;
	}

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = false , foreignKey = @ForeignKey(name = "null"))
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}*/

	/*public OpenButton getOpenButton() {
		return openButton;
	}

	public void setOpenButton(OpenButton openButton) {
		this.openButton = openButton;
	}*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, updatable = true , foreignKey = @ForeignKey(name = "null"))
	public AssCustomerRelation getAssCustomerRelation() {
		return assCustomerRelation;
	}

	public void setAssCustomerRelation(AssCustomerRelation assCustomerRelation) {
		this.assCustomerRelation = assCustomerRelation;
	}

	/*@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@Column(nullable = false, precision = 21, scale = 6)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}*/

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@Valid
	@Column(length = 4000)
	@Convert(converter = SpecificationItemConverter.class)
	public List<SpecificationItem> getSpecificationItems() {
		return specificationItems;
	}

	public void setSpecificationItems(List<SpecificationItem> specificationItems) {
		this.specificationItems = specificationItems;
	}
	
	@Transient
	public boolean hasSpecification() {
		return CollectionUtils.isNotEmpty(getSpecificationItems());
	}
	
	@Transient
	public AssProduct getDefaultProduct() {
		return (AssProduct) CollectionUtils.find(getAssProducts(), new Predicate() {
			public boolean evaluate(Object object) {
				AssProduct assProduct = (AssProduct) object;
				return assProduct != null;
			}
		});
	}
	
	@Converter
	public static class ProductImageConverter extends BaseAttributeConverter<List<ProductImage>> implements AttributeConverter<Object, String> {
	}
	
	@Converter
	public static class SpecificationItemConverter extends BaseAttributeConverter<List<SpecificationItem>> implements AttributeConverter<Object, String> {
	}
	
	@Converter
	public static class LabelConverter extends BaseAttributeConverter<List<Label>> implements AttributeConverter<Object, String> {
	}
	
	@Converter
	public static class ImageConverter extends BaseAttributeConverter<List<String>> implements AttributeConverter<Object, String> {
	}
	
	@Valid
	@Column(length = 4000)
	@Convert(converter = ProductImageConverter.class)
	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = LabelConverter.class)
	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	public String getDetailsDescription() {
		return detailsDescription;
	}

	public void setDetailsDescription(String detailsDescription) {
		this.detailsDescription = detailsDescription;
	}

	@Valid
	@Column(length = 4000)
	@Convert(converter = ImageConverter.class)
	@Pattern(regexp = "^(?i)(http:\\/\\/|https:\\/\\/|\\/).*$")
	public List<String> getDetailsImage() {
		return detailsImage;
	}

	public void setDetailsImage(List<String> detailsImage) {
		this.detailsImage = detailsImage;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public AssGoods getSource() {
		return source;
	}

	public void setSource(AssGoods source) {
		this.source = source;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Lob
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.YES)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
}

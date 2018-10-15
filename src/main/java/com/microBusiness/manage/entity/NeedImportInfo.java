package com.microBusiness.manage.entity;

import com.microBusiness.manage.BaseAttributeConverter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：批量导入个体客户信息记录
 * 修改记录：
 */
@Entity
@Table(name = "t_need_import_info")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_need_import_info")
public class NeedImportInfo extends BaseEntity<Long> {

    private static final long serialVersionUID = 3654994419022700204L;

    private String name ;

    private String userName ;

    private String tel ;

    private Area area ;

    private String address ;

    private Supplier supplier ;

    private String description ;

    private boolean valid;
    
    // 客户编号
  	private String clientNum;

    /**
     * 收货点类型
     */
    public enum Type{
        //普通收货点
        general,
        //流水收货点
        turnover
    }

    private Type type = Type.general ;

    private NeedImportLog needImportLog ;

   	//店员姓名
   	private String clerk;

    List<NeedImportError> errors ;

    /**
     * excel 里的行号
     */
    int rowNum ;

    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @NotEmpty
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NotEmpty
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "null"))
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "null"))
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public String getHiddenTel() {
        return tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Convert(converter = NeedImportInfo.NeedImportErrorConverter.class)
    public List<NeedImportError> getErrors() {
        return errors;
    }

    public void setErrors(List<NeedImportError> errors) {
        this.errors = errors;
    }

    @Converter
    public static class NeedImportErrorConverter extends BaseAttributeConverter<List<NeedImportError>> implements AttributeConverter<Object, String> {
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false ,foreignKey = @ForeignKey(name = "null"))
    public NeedImportLog getNeedImportLog() {
        return needImportLog;
    }

    public void setNeedImportLog(NeedImportLog needImportLog) {
        this.needImportLog = needImportLog;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Transient
    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

	public String getClerk() {
		return clerk;
	}

	public void setClerk(String clerk) {
		this.clerk = clerk;
	}
    

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}
    
}

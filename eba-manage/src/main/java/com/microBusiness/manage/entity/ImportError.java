package com.microBusiness.manage.entity;

import java.io.Serializable;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 下午4:50
 * Describe:
 * Update:
 */
public class ImportError implements Serializable {
    /**
     * 错误字段
     */
    private String errorField ;
    /**
     * 错误信息
     */
    private String errorInfo ;

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}

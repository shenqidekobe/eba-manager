package com.microBusiness.manage.entity.ass;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by mingbai on 2017/11/2.
 * 功能描述：小程序模版 实体
 * 修改记录：
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmallTemplateInfo {

    @JsonProperty(value = "touser")
    private String toUser ;

    @JsonProperty(value = "template_id")
    private String templateId;

    @JsonProperty(value = "form_id")
    private String formId ;

    private String page;

    private Map<String ,Map<String , String>> data;

    private String color ;

    @JsonProperty(value = "emphasis_keyword")
    private String emphasisKeyword ;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmphasisKeyword() {
        return emphasisKeyword;
    }

    public void setEmphasisKeyword(String emphasisKeyword) {
        this.emphasisKeyword = emphasisKeyword;
    }
}

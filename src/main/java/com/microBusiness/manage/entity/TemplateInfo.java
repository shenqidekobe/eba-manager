package com.microBusiness.manage.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by afei.
 * User: afei
 * Date: 2016/6/8 16:55
 * Describe:
 * Update:
 */
@JsonInclude(value=Include.NON_NULL)
public class TemplateInfo {
    @JsonProperty(value = "touser")
    private String toUser ;
    @JsonProperty(value = "template_id")
    private String templateId;
    @JsonProperty(value = "form_id")
    private String formId;
    private String url;
    private String page;
    private Map<String, String> miniprogram; 

    private Map<String ,Map<String , String>> data;

    public TemplateInfo() {
    }
    public TemplateInfo(String toUser, String templateId, String url, Map<String, Map<String, String>> data) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.url = url;
        this.data = data;
    }
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Map<String, Map<String, String>> getData() {
        return data;
    }
    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }
	public Map<String, String> getMiniprogram() {
		return miniprogram;
	}
	public void setMiniprogram(Map<String, String> miniprogram) {
		this.miniprogram = miniprogram;
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
    
}

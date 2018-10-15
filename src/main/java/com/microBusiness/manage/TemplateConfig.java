/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.microBusiness.manage.util.FreeMarkerUtils;
import freemarker.template.TemplateException;

public class TemplateConfig implements Serializable {

	private static final long serialVersionUID = -517540800437045215L;

	public static final String CACHE_NAME = "templateConfig";

	public enum Type {

		page,

		print,

		mail,

		sms
	}

	private String id;

	private Type type;

	private String name;

	private String templatePath;

	private String staticPath;

	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getStaticPath() {
		return staticPath;
	}

	public void setStaticPath(String staticPath) {
		this.staticPath = staticPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRealTemplatePath() {
		return getRealTemplatePath(null);
	}

	public String getRealTemplatePath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getTemplatePath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public String getRealStaticPath() {
		return getRealStaticPath(null);
	}

	public String getRealStaticPath(Map<String, Object> model) {
		try {
			return FreeMarkerUtils.process(getStaticPath(), model);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
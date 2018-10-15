/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.microBusiness.manage.BaseAttributeConverter;

import com.microBusiness.manage.BaseAttributeConverter;
import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "xx_plugin_config")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "seq_plugin_config")
public class PluginConfig extends OrderEntity<Long> {

	private static final long serialVersionUID = -6464289808436677356L;

	private String pluginId;

	private Boolean isEnabled;

	private Map<String, String> attributes = new HashMap<String, String>();

	@Column(nullable = false, updatable = false, unique = true)
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	@Column(nullable = false)
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Column(length = 4000)
	@Convert(converter = MapConverter.class)
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Transient
	public String getAttribute(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		return getAttributes() != null ? getAttributes().get(name) : null;
	}

	@Converter
	public static class MapConverter extends BaseAttributeConverter<Map<String, String>> implements AttributeConverter<Object, String> {
	}

}

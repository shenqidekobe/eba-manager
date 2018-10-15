/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.plugin;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import com.microBusiness.manage.entity.PluginConfig;
import com.microBusiness.manage.service.PluginConfigService;

import com.microBusiness.manage.entity.PluginConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

public abstract class StoragePlugin implements Comparable<StoragePlugin> {

	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	public String getId() {
		return getClass().getAnnotation(Component.class).value();
	}

	public abstract String getName();

	public abstract String getVersion();

	public abstract String getAuthor();

	public abstract String getSiteUrl();

	public abstract String getInstallUrl();

	public abstract String getUninstallUrl();

	public abstract String getSettingUrl();

	public boolean getIsInstalled() {
		return pluginConfigService.pluginIdExists(getId());
	}

	public PluginConfig getPluginConfig() {
		return pluginConfigService.findByPluginId(getId());
	}

	public boolean getIsEnabled() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getIsEnabled() : false;
	}

	public String getAttribute(String name) {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	public Integer getOrder() {
		PluginConfig pluginConfig = getPluginConfig();
		return pluginConfig != null ? pluginConfig.getOrder() : null;
	}

	public abstract void upload(String path, File file, String contentType);

	public abstract String getUrl(String path);

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		StoragePlugin other = (StoragePlugin) obj;
		return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
	}

	public int compareTo(StoragePlugin storagePlugin) {
		if (storagePlugin == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), storagePlugin.getOrder()).append(getId(), storagePlugin.getId()).toComparison();
	}

	public void upload(String path, File file, String contentType , boolean useAbsolutePath){
		File destFile = new File(path);
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
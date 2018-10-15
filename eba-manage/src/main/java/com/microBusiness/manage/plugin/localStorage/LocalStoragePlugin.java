/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.plugin.localStorage;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.microBusiness.manage.Setting;
import com.microBusiness.manage.plugin.StoragePlugin;
import com.microBusiness.manage.util.SystemUtils;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component("localStoragePlugin")
public class LocalStoragePlugin extends StoragePlugin implements ServletContextAware {

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String getName() {
		return "本地文件存储";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "utlz";
	}

	@Override
	public String getSiteUrl() {
		return "http://www.dreamforyou";
	}

	@Override
	public String getInstallUrl() {
		return null;
	}

	@Override
	public String getUninstallUrl() {
		return null;
	}

	@Override
	public String getSettingUrl() {
		return "local_storage/setting.jhtml";
	}

	@Override
	public void upload(String path, File file, String contentType) {
		File destFile = new File(servletContext.getRealPath(path));
		try {
			FileUtils.moveFile(file, destFile);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getUrl(String path) {
		Setting setting = SystemUtils.getSetting();
		return setting.getFileSystemUrl() + path;
	}

	@Override
	public void upload(String path, File file, String contentType, boolean useAbsolutePath) {
		if(useAbsolutePath){
			super.upload(path, file, contentType , useAbsolutePath);
		}else{
			this.upload(path, file, contentType);
		}
	}

}
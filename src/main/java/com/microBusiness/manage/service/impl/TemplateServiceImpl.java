/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.service.TemplateService;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.SystemUtils;

import com.microBusiness.manage.TemplateConfig;
import com.microBusiness.manage.util.SystemUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import freemarker.template.TemplateException;

@Service("templateServiceImpl")
public class TemplateServiceImpl implements TemplateService, ServletContextAware {

	private ServletContext servletContext;

	@Value("${template.loader_path}")
	private String templateLoaderPath;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String read(String templateConfigId) {
		Assert.hasText(templateConfigId);

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		return read(templateConfig);
	}

	public String read(TemplateConfig templateConfig) {
		Assert.notNull(templateConfig);

		try {
			String templatePath = servletContext.getRealPath(templateLoaderPath + FreeMarkerUtils.process(templateConfig.getRealTemplatePath()));
			File templateFile = new File(templatePath);
			return FileUtils.readFileToString(templateFile, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void write(String templateConfigId, String content) {
		Assert.hasText(templateConfigId);

		TemplateConfig templateConfig = SystemUtils.getTemplateConfig(templateConfigId);
		write(templateConfig, content);
	}

	public void write(TemplateConfig templateConfig, String content) {
		Assert.notNull(templateConfig);

		try {
			String templatePath = servletContext.getRealPath(templateLoaderPath + FreeMarkerUtils.process(templateConfig.getRealTemplatePath()));
			File templateFile = new File(templatePath);
			FileUtils.writeStringToFile(templateFile, content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
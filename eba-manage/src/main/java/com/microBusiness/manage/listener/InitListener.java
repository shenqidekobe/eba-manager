/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.listener;

import java.io.File;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.microBusiness.manage.service.ConfigService;
import com.microBusiness.manage.service.SearchService;
import com.microBusiness.manage.service.StaticService;

import com.microBusiness.manage.service.ConfigService;
import com.microBusiness.manage.service.SearchService;
import com.microBusiness.manage.service.StaticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component("initListener")
public class InitListener implements ServletContextAware, ApplicationListener<ContextRefreshedEvent> {

	private static final String INSTALL_INIT_CONFIG_FILE_PATH = "/install_init.conf";

	private static final Logger LOGGER = Logger.getLogger(InitListener.class.getName());

	private ServletContext servletContext;

	@Value("${system.version}")
	private String systemVersion;
	@Resource(name = "configServiceImpl")
	private ConfigService configService;
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	@Resource(name = "searchServiceImpl")
	private SearchService searchService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (servletContext != null && contextRefreshedEvent.getApplicationContext().getParent() == null) {
			String info = "I|n|i|t|i|a|l|i|z|i|n|g| |U|T|L|Z| |" + systemVersion;
			LOGGER.info(info.replace("|", ""));
			configService.init();
			/*File installInitConfigFile = new File(servletContext.getRealPath(INSTALL_INIT_CONFIG_FILE_PATH));
			if (installInitConfigFile.exists()) {
				staticService.generateAll();
				searchService.purge();
				searchService.index();
				installInitConfigFile.delete();
			} else {
				staticService.generateIndex();
				staticService.generateOther();
			}*/
		}
	}

}
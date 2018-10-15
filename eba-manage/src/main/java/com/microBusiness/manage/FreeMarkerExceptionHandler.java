/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage;

import java.io.Writer;

import com.microBusiness.manage.exception.IllegalLicenseException;

import com.microBusiness.manage.exception.IllegalLicenseException;
import org.apache.commons.lang.exception.ExceptionUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerExceptionHandler implements TemplateExceptionHandler {

	private static final TemplateExceptionHandler DEFAULT_TEMPLATE_EXCEPTION_HANDLER = TemplateExceptionHandler.DEBUG_HANDLER;

	public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
		if (ExceptionUtils.indexOfThrowable(te, IllegalLicenseException.class) >= 0) {
			throw new IllegalLicenseException();
		}
		DEFAULT_TEMPLATE_EXCEPTION_HANDLER.handleTemplateException(te, env, out);
	}

}
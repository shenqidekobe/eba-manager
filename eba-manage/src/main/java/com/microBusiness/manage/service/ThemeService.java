/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.Theme;

import com.microBusiness.manage.Theme;
import org.springframework.web.multipart.MultipartFile;

public interface ThemeService {

	List<Theme> getAll();

	Theme get(String id);

	boolean upload(MultipartFile multipartFile);

}
/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.entity.ProductImage;

import java.util.List;

public interface OrderFileService {

	void filter(List<OrderFile> orderFiles);

	boolean isValid(OrderFile orderFile);

	void generate(OrderFile orderFile);

	void generate(List<OrderFile> orderFile);

	void generate(List<OrderFile> orderFiles , boolean useAbsolutePath);

	void generate(OrderFile orderFile , boolean useAbsolutePath);

}
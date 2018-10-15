/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import java.util.List;

import com.microBusiness.manage.entity.ProductImage;
import com.microBusiness.manage.entity.ProductImage;

public interface ProductImageService {

	void filter(List<ProductImage> productImages);

	boolean isValid(ProductImage productImage);

	void generate(ProductImage productImage);

	void generate(List<ProductImage> productImages);

}
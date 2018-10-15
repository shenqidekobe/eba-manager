/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service;

import com.microBusiness.manage.FileType;

import com.microBusiness.manage.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	boolean isValid(FileType fileType, MultipartFile multipartFile);

	String upload(FileType fileType, MultipartFile multipartFile, boolean async);

	String upload(FileType fileType, MultipartFile multipartFile);

	String uploadLocal(FileType fileType, MultipartFile multipartFile);

	/**
	 *
	 * @param fileType
	 * @param multipartFile
	 * @param async
	 * @param useAbsolutePath 是否使用绝对路径
	 * @return
	 */
	String upload(FileType fileType, MultipartFile multipartFile, boolean async , boolean useAbsolutePath);

}
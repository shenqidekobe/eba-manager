/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.microBusiness.manage.FileType;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.ImgType;
import com.microBusiness.manage.plugin.StoragePlugin;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.service.PluginService;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.ImgCompress;
import com.microBusiness.manage.util.SystemUtils;

import freemarker.template.TemplateException;

@Service("fileServiceImpl")
public class FileServiceImpl implements FileService, ServletContextAware {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Value("${upload.absolutePath.pre}")
	private String absolutePathPre ;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void addUploadTask(final StoragePlugin storagePlugin, final String path, final File file, final String contentType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				upload(storagePlugin, path, file, contentType);
			}
		});
	}

	private void upload(StoragePlugin storagePlugin, String path, File file, String contentType) {
		Assert.notNull(storagePlugin);
		Assert.hasText(path);
		Assert.notNull(file);
		Assert.hasText(contentType);

		try {
			storagePlugin.upload(path, file, contentType);
		} finally {
			FileUtils.deleteQuietly(file);
		}
	}

	public boolean isValid(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType);
		Assert.notNull(multipartFile);
		Assert.state(!multipartFile.isEmpty());

		Setting setting = SystemUtils.getSetting();
		if (setting.getUploadMaxSize() != null && setting.getUploadMaxSize() != 0 && multipartFile.getSize() > setting.getUploadMaxSize() * 1024L * 1024L) {
			return false;
		}
		String[] uploadExtensions;
		switch (fileType) {
		case media:
			uploadExtensions = setting.getUploadMediaExtensions();
			break;
		case file:
			uploadExtensions = setting.getUploadFileExtensions();
			break;
		default:
			uploadExtensions = setting.getUploadImageExtensions();
			break;
		}
		if (ArrayUtils.isNotEmpty(uploadExtensions)) {
			return FilenameUtils.isExtension(multipartFile.getOriginalFilename().toLowerCase(), uploadExtensions);
		}
		return false;
	}

	public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
		Assert.notNull(fileType);
		Assert.notNull(multipartFile);
		Assert.state(!multipartFile.isEmpty());

		Setting setting = SystemUtils.getSetting();
		String uploadPath;
		switch (fileType) {
		case media:
			uploadPath = setting.getMediaUploadPath();
			break;
		case file:
			uploadPath = setting.getFileUploadPath();
			break;
		default:
			uploadPath = setting.getImageUploadPath();
			break;
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreeMarkerUtils.process(uploadPath, model);
			String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);
				String contentType = multipartFile.getContentType();
				if (async) {
					addUploadTask(storagePlugin, destPath, tempFile, contentType);
				} else {
					upload(storagePlugin, destPath, tempFile, contentType);
				}
				return storagePlugin.getUrl(destPath);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return null;
	}

	public String upload(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType);
		Assert.notNull(multipartFile);
		Assert.state(!multipartFile.isEmpty());

		return upload(fileType, multipartFile, true);
	}

	public String uploadLocal(FileType fileType, MultipartFile multipartFile) {
		Assert.notNull(fileType);
		Assert.notNull(multipartFile);
		Assert.state(!multipartFile.isEmpty());

		Setting setting = SystemUtils.getSetting();
		String uploadPath;
		switch (fileType) {
		case media:
			uploadPath = setting.getMediaUploadPath();
			break;
		case file:
			uploadPath = setting.getFileUploadPath();
			break;
		default:
			uploadPath = setting.getImageUploadPath();
			break;
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreeMarkerUtils.process(uploadPath, model);
			String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			File destFile = new File(servletContext.getRealPath(destPath));
			new File(path).mkdirs();
			multipartFile.transferTo(destFile);
			return destPath;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * @param fileType
	 * @param multipartFile
	 * @param async
	 * @param useAbsolutePath 是否使用绝对路径
	 * @return
	 */
	@Override
	public String upload(FileType fileType, MultipartFile multipartFile, boolean async, boolean useAbsolutePath) {

		Assert.notNull(fileType);
		Assert.notNull(multipartFile);
		Assert.state(!multipartFile.isEmpty());

		Setting setting = SystemUtils.getSetting();
		String uploadPath;
		switch (fileType) {
			case media:
				uploadPath = setting.getMediaUploadPath();
				break;
			case file:
				uploadPath = setting.getFileUploadPath();
				break;
			default:
				uploadPath = setting.getImageUploadPath();
				break;
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreeMarkerUtils.process(uploadPath, model);
			String destPath , storePath ;

			destPath = storePath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename()) ;

			if(useAbsolutePath && StringUtils.isNotEmpty(this.absolutePathPre)){
				storePath = absolutePathPre + storePath ;
			}

			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);
				String contentType = multipartFile.getContentType();
				if (async) {
					addUploadTask(storagePlugin, storePath, tempFile, contentType , useAbsolutePath);
				} else {
					upload(storagePlugin, storePath, tempFile, contentType , useAbsolutePath);
					//压缩图片
					if(fileType == FileType.image) {
//		            	String[] paths=storePath.split("\\.");
//		            	String destMediumPath=paths[0]+"-"+ImgType.medium+"."+paths[1];
//		            	String destSmallPath=paths[0]+"-"+ImgType.small+"."+paths[1];
//		            	if (useAbsolutePath) {
//		            		Thumbnails.of(storePath).size(500, 500).toFile(destMediumPath);
//		            		Thumbnails.of(storePath).size(300, 300).toFile(destSmallPath);
//		            	}else {
//		            		Thumbnails.of(servletContext.getRealPath(storePath)).size(500, 500).toFile(servletContext.getRealPath(destMediumPath));
//		            		Thumbnails.of(servletContext.getRealPath(storePath)).size(300, 300).toFile(servletContext.getRealPath(destSmallPath));
//		            	}
//		            	logger.info("压缩图片成功：中图： " + destMediumPath);
//		            	logger.info("压缩图片成功：小图： " + destSmallPath);
						
						String[] paths=storePath.split("\\.");
		            	String destMediumPath=paths[0]+"-"+ImgType.medium+"."+paths[1];
		            	String destSmallPath=paths[0]+"-"+ImgType.small+"."+paths[1];
		            	ImgCompress imgCompress = new ImgCompress(storePath);
		            	imgCompress.resize(imgCompress.getWidth(), imgCompress.getHeight(), destMediumPath);
		            	imgCompress.resize(imgCompress.getWidth()/2, imgCompress.getWidth()/2, destSmallPath);
						
		            }
					
					
				}
				return storagePlugin.getUrl(destPath);
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return null;
	}

	private void upload(StoragePlugin storagePlugin, String path, File file, String contentType , boolean useAbsolutePath) {
		Assert.notNull(storagePlugin);
		Assert.hasText(path);
		Assert.notNull(file);
		Assert.hasText(contentType);

		try {
			if(useAbsolutePath){
				storagePlugin.upload(path, file, contentType , useAbsolutePath);
			}else{
				storagePlugin.upload(path, file, contentType);
			}
		} finally {
			FileUtils.deleteQuietly(file);
		}
	}

	private void addUploadTask(final StoragePlugin storagePlugin, final String path, final File file, final String contentType , final boolean useAbsolutePath) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				upload(storagePlugin, path, file, contentType , useAbsolutePath);
			}
		});
	}

}
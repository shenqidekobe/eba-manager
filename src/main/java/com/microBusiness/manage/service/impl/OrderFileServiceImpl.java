/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.microBusiness.manage.FileType;
import com.microBusiness.manage.Setting;
import com.microBusiness.manage.entity.OrderFile;
import com.microBusiness.manage.plugin.StoragePlugin;
import com.microBusiness.manage.service.FileService;
import com.microBusiness.manage.service.OrderFileService;
import com.microBusiness.manage.service.PluginService;
import com.microBusiness.manage.util.FreeMarkerUtils;
import com.microBusiness.manage.util.ImageUtils;
import com.microBusiness.manage.util.SystemUtils;

import freemarker.template.TemplateException;

@Service("orderFileServiceImpl")
public class OrderFileServiceImpl implements OrderFileService, ServletContextAware {

	private static final String DEST_EXTENSION = "jpg";

	private static final String DEST_CONTENT_TYPE = "image/jpeg";

	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Value("${upload.absolutePath.pre}")
	private String absolutePathPre ;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void addTask(final StoragePlugin storagePlugin, final String sourcePath, final String largePath, final String mediumPath, final String thumbnailPath, final File tempFile, final String contentType , final boolean useAbsolutePath) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				Setting setting = SystemUtils.getSetting();
				try {
					/*ImageUtils.zoom(tempFile, largeTempFile, setting.getLargeorderFileWidth(), setting.getLargeorderFileHeight());
					ImageUtils.addWatermark(largeTempFile, largeTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, mediumTempFile, setting.getMediumorderFileWidth(), setting.getMediumorderFileHeight());
					ImageUtils.addWatermark(mediumTempFile, mediumTempFile, watermarkFile, setting.getWatermarkPosition(), setting.getWatermarkAlpha());
					ImageUtils.zoom(tempFile, thumbnailTempFile, setting.getThumbnailorderFileWidth(), setting.getThumbnailorderFileHeight());*/
					storagePlugin.upload(sourcePath, tempFile, contentType , useAbsolutePath);
					/*storagePlugin.upload(largePath, largeTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(mediumPath, mediumTempFile, DEST_CONTENT_TYPE);
					storagePlugin.upload(thumbnailPath, thumbnailTempFile, DEST_CONTENT_TYPE);*/
				} finally {
					FileUtils.deleteQuietly(tempFile);
					/*FileUtils.deleteQuietly(largeTempFile);
					FileUtils.deleteQuietly(mediumTempFile);
					FileUtils.deleteQuietly(thumbnailTempFile);*/
				}
			}
		});
	}

	public void filter(List<OrderFile> orderFiles) {
		CollectionUtils.filter(orderFiles, new Predicate() {
			public boolean evaluate(Object object) {
				OrderFile orderFile = (OrderFile) object;
				return orderFile != null && !orderFile.isEmpty() && isValid(orderFile);
			}
		});
	}

	public boolean isValid(OrderFile orderFile) {
		Assert.notNull(orderFile);

		return orderFile.getFile() == null || orderFile.getFile().isEmpty() || fileService.isValid(FileType.image, orderFile.getFile());
	}

	public void generate(OrderFile orderFile) {
		if (orderFile == null || orderFile.getFile() == null || orderFile.getFile().isEmpty()) {
			return;
		}

		try {
			Setting setting = SystemUtils.getSetting();
			MultipartFile multipartFile = orderFile.getFile();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String uploadPath = FreeMarkerUtils.process(setting.getImageUploadPath(), model);
			String uuid = UUID.randomUUID().toString();
			String sourcePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			String largePath = uploadPath + uuid + "-large." + DEST_EXTENSION;
			String mediumPath = uploadPath + uuid + "-medium." + DEST_EXTENSION;
			String thumbnailPath = uploadPath + uuid + "-thumbnail." + DEST_EXTENSION;
			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);
				addTask(storagePlugin, sourcePath, largePath, mediumPath, thumbnailPath, tempFile, multipartFile.getContentType() , false);
				orderFile.setSource(storagePlugin.getUrl(sourcePath));
				orderFile.setSize(multipartFile.getSize());
				orderFile.setTitle(multipartFile.getOriginalFilename());
				break;
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void generate(List<OrderFile> orderFiles) {
		if (CollectionUtils.isEmpty(orderFiles)) {
			return;
		}
		for (OrderFile orderFile : orderFiles) {
			generate(orderFile);
		}
	}

	@Override
	public void generate(List<OrderFile> orderFiles, boolean useAbsolutePath) {
		if (CollectionUtils.isEmpty(orderFiles)) {
			return;
		}
		for (OrderFile orderFile : orderFiles) {
			if(null == orderFile.getFile()){
				continue;
			}
			generate(orderFile , useAbsolutePath);
		}
	}

	@Override
	public void generate(OrderFile orderFile, boolean useAbsolutePath) {
		if (orderFile == null || orderFile.getFile() == null || orderFile.getFile().isEmpty()) {
			return;
		}

		try {
			Setting setting = SystemUtils.getSetting();
			MultipartFile multipartFile = orderFile.getFile();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());

			String uploadPath = FreeMarkerUtils.process(setting.getImageUploadPath(), model);

			String uuid = UUID.randomUUID().toString();
			String destPath , storePath ;

			destPath = storePath = uploadPath + uuid + "-source." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

			if(useAbsolutePath && StringUtils.isNotEmpty(this.absolutePathPre)){
				storePath = absolutePathPre + storePath ;
			}

			for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
				File tempFile = new File(FileUtils.getTempDirectory(), UUID.randomUUID() + ".tmp");
				multipartFile.transferTo(tempFile);

				addTask(storagePlugin, storePath, null, null, null, tempFile, multipartFile.getContentType() , true);

				orderFile.setSource(storagePlugin.getUrl(destPath));
				orderFile.setSize(multipartFile.getSize());
				orderFile.setTitle(multipartFile.getOriginalFilename());
				break;
			}
		} catch (IllegalStateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		
	}
}
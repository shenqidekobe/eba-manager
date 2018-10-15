package com.microBusiness.manage.entity;

import java.io.Serializable;

/**
 * 
 * Created by yuezhiwei on 2017/6/5.
 * 功能描述：备注文件详细信息
 */
public class RemarksFile implements Serializable {
	
	private static final long serialVersionUID = 6295951261521749480L;

	//文件路径
	private String url;
	
	//文件名称
	private String fileName;
	
	//文件大小
	private Long size;
	
	//文件后缀名
	private String suffix;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}

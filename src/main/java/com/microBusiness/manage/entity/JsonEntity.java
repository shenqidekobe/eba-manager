package com.microBusiness.manage.entity;

import com.microBusiness.manage.util.Code;

public class JsonEntity {
	
	private String code;
	private String msg;
	private String requesturl;
	private	Object data;

	private Object data2;

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}

	public JsonEntity(String code, String msg, String requesturl, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.requesturl = requesturl;
		this.data = data;
	}
	
	public JsonEntity(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
		
	}

	public JsonEntity(Code code, String msg) {
		super();
		this.code = "" + code.getCode();
		this.msg = msg;

	}
	public JsonEntity(Code code, String msg, String requesturl, Object data) {
		super();
		this.code = "" + code.getCode();
		this.msg = msg;
		this.requesturl = requesturl;
		this.data = data;
	}

	public JsonEntity() {
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getRequesturl() {
		return requesturl;
	}
	public void setRequesturl(String requesturl) {
		this.requesturl = requesturl;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}


	public static JsonEntity successMessage(Object content){
		return new JsonEntity(Code.code0 , content);
	}

	public static JsonEntity successMessage() {
		return new JsonEntity(Code.code0);
	}

	public static JsonEntity error(Code code , Object content){
		return new JsonEntity(code , content);
	}

	public static JsonEntity error(Code code){
		return new JsonEntity(code);
	}

	public JsonEntity(Code code , Object data) {
		super();
		this.code = "" + code.getCode();
		this.msg = code.getDesc();
		this.data = data;
	}

	public JsonEntity(Code code) {
		super();
		this.code = "" + code.getCode();
		this.msg = code.getDesc();

	}
	
}

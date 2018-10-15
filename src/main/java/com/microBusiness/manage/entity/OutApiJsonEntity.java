package com.microBusiness.manage.entity;

import com.microBusiness.manage.util.Code;

public class OutApiJsonEntity {
	
	private String code;
	private String msg;
	private	Object data;

	public OutApiJsonEntity(String code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public OutApiJsonEntity(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
		
	}

	public OutApiJsonEntity(Code code, String msg) {
		super();
		this.code = "" + code.getCode();
		this.msg = msg;

	}
	public OutApiJsonEntity(Code code, String msg, Object data) {
		super();
		this.code = "" + code.getCode();
		this.msg = msg;
		this.data = data;
	}

	public OutApiJsonEntity() {
		
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
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}


	public static OutApiJsonEntity successMessage(Object content){
		return new OutApiJsonEntity(Code.code0 , content);
	}

	public static OutApiJsonEntity successMessage() {
		return new OutApiJsonEntity(Code.code0);
	}

	public static OutApiJsonEntity error(Code code , Object content){
		return new OutApiJsonEntity(code , content);
	}

	public static OutApiJsonEntity error(Code code){
		return new OutApiJsonEntity(code);
	}

	public OutApiJsonEntity(Code code , Object data) {
		super();
		this.code = "" + code.getCode();
		this.msg = code.getDesc();
		this.data = data;
	}

	public OutApiJsonEntity(Code code) {
		super();
		this.code = "" + code.getCode();
		this.msg = code.getDesc();

	}
	
}

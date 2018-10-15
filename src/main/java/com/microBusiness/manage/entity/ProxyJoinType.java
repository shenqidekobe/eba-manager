package com.microBusiness.manage.entity;

public enum ProxyJoinType {

		SYS("系统创建"),
		SELF_DIRECT("自己申请"),
		PARENT("上级介绍");
		
		private String name;
		
		ProxyJoinType(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
}

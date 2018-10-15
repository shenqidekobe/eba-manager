package com.microBusiness.manage.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public final class CookieUtil {
	
	/**
	 * 添加cookie
	 * @param response 
	 * @param name 存入的key值
	 * @param value 存入的value值
	 * @param path  读取的路径的值
	 * @param maxAge cookie的声明周期
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,String path,int maxAge){
		response.setContentType("text/html;charset=utf-8");
		Cookie cookie = new Cookie(name,value);
		cookie.setPath(path);
		//cookie.setDomain("/") ;  //域名要以“.”开头
		if(maxAge>0){
			cookie.setMaxAge(maxAge);
		}  
		response.addCookie(cookie);//发送cookie文件
	}
	
	/**
	 * 获取cookie里的值
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	        	cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	
	/**
	 * 修改cookie里的值
	 * @param response
	 * @param request
	 * @param name
	 * @param value
	 * @param path
	 * @param maxAge
	 */
	public static void editCookie(HttpServletResponse response,HttpServletRequest request,String name,String value,String path,int maxAge){
		response.setContentType("text/html;charset=utf-8");
		Cookie cookies[] = request.getCookies() ;
        Cookie cookie = null ;
        for(int i=0;i<cookies.length;i++){
        	cookie = cookies[i] ;
            if(cookie.getName().equals(name)){
            	cookie.setValue(value);
            	cookie.setMaxAge(maxAge) ;
            	cookie.setPath(path);
       			//cookie.setDomain(".zl.org") ;
       			response.addCookie(cookie) ;     //修改后，要更新到浏览器中    
            }
        }
	}
	
	/**
	 * 删除cookie
	 * @param response
	 * @param request
	 * @param name
	 */
	public static void deleteCookie(HttpServletResponse response,HttpServletRequest request,String name,String path){
		response.setContentType("text/html;charset=utf-8");
		Cookie cookies[] = request.getCookies() ;
       	Cookie cookie = null ;
       	if(cookies == null || cookies.length <= 0){
       		return;
       	}
       	for(int i=0;i<cookies.length;i++){
       		cookie = cookies[i] ;
       		if(cookie != null && StringUtils.isNotBlank(cookie.getName())){
       			if(cookie.getName().equals(name)){
           			Cookie newCookie = new Cookie(name, "");  
           			newCookie.setMaxAge(0);
           			newCookie.setPath(path);
           			response.addCookie(cookie) ;
           		}
       		}
       	}
	}
}

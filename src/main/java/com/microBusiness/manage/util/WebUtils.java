/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microBusiness.manage.Setting;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public final class WebUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

	private static final PoolingHttpClientConnectionManager HTTP_CLIENT_CONNECTION_MANAGER;

	private static CloseableHttpClient HTTP_CLIENT;

	private static final Charset CHARSET ;

	static {
		HTTP_CLIENT_CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build());
		HTTP_CLIENT_CONNECTION_MANAGER.setDefaultMaxPerRoute(100);
		HTTP_CLIENT_CONNECTION_MANAGER.setMaxTotal(200);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000).setConnectTimeout(60000)
				.setSocketTimeout(60000).setRelativeRedirectsAllowed(true)
				.build();
		HTTP_CLIENT = HttpClientBuilder.create()
				.setConnectionManager(HTTP_CLIENT_CONNECTION_MANAGER)
				.setDefaultRequestConfig(requestConfig).build();
		CHARSET = Charset.forName("UTF-8") ;
		
	}

	private WebUtils() {
	}
	
	public static void setWeixinCertP12(String p12Path) {
		try {
			HTTP_CLIENT = (CloseableHttpClient) ClientCustomSSL.getHttpClientWithP12(p12Path);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("WebUtils load p12Path...error", e);
		}
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge, String path, String domain, Boolean secure) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		Assert.hasText(value);

		try {
			name = URLEncoder.encode(name, "UTF-8");
			value = URLEncoder.encode(value, "UTF-8");
			Cookie cookie = new Cookie(name, value);
			if (maxAge != null) {
				cookie.setMaxAge(maxAge);
			}
			if (StringUtils.isNotEmpty(path)) {
				cookie.setPath(path);
			}
			if (StringUtils.isNotEmpty(domain)) {
				cookie.setDomain(domain);
			}
			if (secure != null) {
				cookie.setSecure(secure);
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, Integer maxAge) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		Assert.hasText(value);

		Setting setting = SystemUtils.getSetting();
		addCookie(request, response, name, value, maxAge, setting.getCookiePath(), setting.getCookieDomain(), null);
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);
		Assert.hasText(value);

		Setting setting = SystemUtils.getSetting();
		addCookie(request, response, name, value, null, setting.getCookiePath(), setting.getCookieDomain(), null);
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Assert.hasText(name);

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			try {
				name = URLEncoder.encode(name, "UTF-8");
				for (Cookie cookie : cookies) {
					if (name.equals(cookie.getName())) {
						return URLDecoder.decode(cookie.getValue(), "UTF-8");
					}
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return null;
	}

	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String path, String domain) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);

		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie cookie = new Cookie(name, null);
			cookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path)) {
				cookie.setPath(path);
			}
			if (StringUtils.isNotEmpty(domain)) {
				cookie.setDomain(domain);
			}
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Assert.notNull(request);
		Assert.notNull(response);
		Assert.hasText(name);

		Setting setting = SystemUtils.getSetting();
		removeCookie(request, response, name, setting.getCookiePath(), setting.getCookieDomain());
	}

	public static Map<String, String> parse(String query, String encoding) {
		Assert.hasText(query);

		Charset charset;
		if (StringUtils.isNotEmpty(encoding)) {
			charset = Charset.forName(encoding);
		} else {
			charset = Charset.forName("UTF-8");
		}
		List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(query, charset);
		Map<String, String> parameterMap = new HashMap<String, String>();
		for (NameValuePair nameValuePair : nameValuePairs) {
			parameterMap.put(nameValuePair.getName(), nameValuePair.getValue());
		}
		return parameterMap;
	}

	public static Map<String, String> parse(String query) {
		Assert.hasText(query);

		return parse(query, null);
	}

	public static String post(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);

		LOGGER.info("request info : url : {} , params:{}" , url , parameterMap.toString());

		String result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity , CHARSET);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		LOGGER.info("response info :{}" , result);

		return result;
	}

	public static String get(String url, Map<String, Object> parameterMap) {
		Assert.hasText(url);

		LOGGER.info("request info : url : {} , params:{}" , url , parameterMap.toString());

		String result = null;
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (parameterMap != null) {
				for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String value = ConvertUtils.convert(entry.getValue());
					if (StringUtils.isNotEmpty(name)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					}
				}
			}
			HttpGet httpGet = new HttpGet(url + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity , CHARSET);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		LOGGER.info("response info :{}" , result);

		return result;
	}

	public static String post(String url, String xmlParams) {
		LOGGER.info("request info : url : {} , params:{}" , url , xmlParams);
		Assert.hasText(url);
		String result = null;
		try {
			StringEntity json = new StringEntity(xmlParams , CHARSET);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(json);
			CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
			try {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					result = EntityUtils.toString(httpEntity , CHARSET);
					EntityUtils.consume(httpEntity);
				}
			} finally {
				try {
					httpResponse.close();
				} catch (IOException e) {
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		LOGGER.info("response info :{}" , result);
		return result;
	}

	public static CloseableHttpResponse httpPostWithJSON(String url, String jsonParams)
            throws Exception {
		
		LOGGER.info("request info : url : {} , params:{}" , url , jsonParams);

		Assert.hasText(url);

		Map map = new HashMap<>();
		
		CloseableHttpResponse httpResponse = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			StringEntity json = new StringEntity(jsonParams , "utf-8");
//			json.setContentType("application/json");
//			json.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//	                        "UTF-8"));
	        httpPost.setEntity(json);
			httpResponse = HTTP_CLIENT.execute(httpPost);
			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		LOGGER.info("response info :{}" , httpResponse.getEntity());
		return httpResponse;
		
//		LOGGER.info("url :{} , data :{}" , url, json);
//		
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
//
//        StringEntity se = new StringEntity(json);
//        se.setContentType("application/json");
//        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
//                        "UTF-8"));
//        httpPost.setEntity(se);
//        HttpResponse response = httpClient.execute(httpPost);
//
//        LOGGER.info("small response :{}" , response);
//
//        if (response != null) {
//            HttpEntity resEntity = response.getEntity();
//            if (resEntity != null) {
//
//               InputStream instreams = resEntity.getContent();
//
//               return instreams;
////             String uploadSysUrl = path;
////             result = path+id+".jpg";
////             ImageUtils.saveToImgByInputStream(instreams, uploadSysUrl, id+".jpg");
//            }
//        }
//
//        httpPost.abort();
//        return null;
    }

}
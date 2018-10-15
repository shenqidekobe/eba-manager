package com.microBusiness.manage.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 得到系统的信息Ip地址
 * 
 * @author xhs
 * 
 */
public class SystemUtil {

	public static void main(String[] args) {
		System.out.println("host ip:" + getHostIp());

		InetAddress netAddress = getInetAddress();
		System.out.println("host ip:" + getHostIp(netAddress));
		System.out.println("host name:" + getHostName(netAddress));
		Properties properties = System.getProperties();
		Set<String> set = properties.stringPropertyNames(); // 获取java虚拟机和系统的信息。
		for (String name : set) {
			System.out.println(name + ":" + properties.getProperty(name));
		}
	}

	/**
	 * 得到地址InetAddress
	 * 
	 * @return 地址InetAddress
	 */
	public static InetAddress getInetAddress() {

		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("unknown host!");
		}
		return null;

	}

	/**
	 * 得到HostIp
	 * 
	 * @return ip地址字符串
	 */
	public static String getHostIp() {
		InetAddress netAddress = getInetAddress();
		if (null == netAddress) {
			return null;
		}
		String ip = netAddress.getHostAddress(); // get the ip address
		return ip;
	}

	/**
	 * 得到HostName
	 * 
	 * @return 主机名字符串
	 */
	public static String getHostName() {
		InetAddress netAddress = getInetAddress();
		if (null == netAddress) {
			return null;
		}
		String name = netAddress.getHostName(); // get the host address
		return name;
	}

	/**
	 * 得到HostIp
	 * 
	 * @param netAddress
	 * @return ip地址字符串
	 */
	public static String getHostIp(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String ip = netAddress.getHostAddress(); // get the ip address
		return ip;
	}

	/**
	 * 得到HostName
	 * 
	 * @param netAddress
	 * @return 主机名字符串
	 */
	public static String getHostName(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String name = netAddress.getHostName(); // get the host address
		return name;
	}

	/**
	 * 获取客户端的ip
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 客户端的ip地址
	 */
	public static String getRemoteIP(HttpServletRequest request) {

		if (null == request) {
			return null;
		}
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}

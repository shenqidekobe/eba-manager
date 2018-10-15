package com.microBusiness.manage.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XmlUtils {

	public static String parseOpenIDFromXml(String xml) throws Exception {
		if (xml == null || xml.equals(""))
			return "";
		Document document = DocumentHelper.parseText(xml);
		// 获取根元素
		Element root = document.getRootElement();
		// 获取名字为指定名称的第一个子元素
		Element firstWorldElement = root.element("FromUserName");
		return firstWorldElement.getTextTrim();
	}

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map<String,String> doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String,String> m = new HashMap<String,String>();
		InputStream in = new ByteArrayInputStream(strxml.getBytes());
		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document doc = builder.build(in);
		org.jdom.Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			org.jdom.Element e = (org.jdom.Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// 关闭流
		in.close();

		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				org.jdom.Element e = (org.jdom.Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param return_code
	 * @param return_msg
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		// <xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	public static void main(String[] args) throws Exception {
		String xml = "<xml><ToUserName><![CDATA[gh_fa435996049e]]></ToUserName><FromUserName><![CDATA[ogeiwuEgUZqvofkpnUM3WQP7PExc]]></FromUserName><CreateTime>1435045881</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[浣犲ソ]]></Content><MsgId>6163475127361417282</MsgId></xml>";
		String openID = parseOpenIDFromXml(xml);
		System.out.println(openID);
	}
}

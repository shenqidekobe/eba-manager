package com.microBusiness.manage.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * xml工具类
 * 
 * @author xhs
 *
 */
public class XmlUtil {

	/**
	 * SortedMap转换得到最终提交的xml
	 * 
	 * @param parameters
	 *            参数map
	 * @return 得到字符串的xml数据
	 */
	public static String getXml(SortedMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v)) {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		System.out.println("最终提交的xml:" + sb.toString());
		return sb.toString();
	}

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return 将xml字符串数据转换成Map
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static Map doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Map m = new HashMap();
		InputStream in = new ByteArrayInputStream(strxml.getBytes());
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
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
	 *            子节点名称
	 * @return String 子结点xml
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

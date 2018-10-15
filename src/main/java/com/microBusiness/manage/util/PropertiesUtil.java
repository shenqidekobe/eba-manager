package com.microBusiness.manage.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

/**
 * 实现对Java配置文件Properties的读取
 * 
 * @author xhs
 *
 */
public class PropertiesUtil {

	// 属性文件的路径
	static String profilepath = "/pay.properties";
	/**
	 * 采用静态方法得到配置文件的Properties
	 */
	private static Properties props = new Properties();

	static {
		try {
			File file = new ClassPathResource(profilepath).getFile();
			FileInputStream fileInputStream = new FileInputStream(file);
			props.load(fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.exit(-1);
		}
	}

	/**
	 * 读取属性文件中相应键的值
	 * 
	 * @param key
	 *            主键
	 * @return String
	 */
	public static String getKeyValue(String key) {
		return props.getProperty(key);
	}

	/**
	 * 根据主键key读取主键的值value
	 * 
	 * @param filePath
	 *            属性文件路径
	 * @param key
	 *            键名
	 */
	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			// InputStream in = new BufferedInputStream(new
			// FileInputStream(filePath));
			File file = new ClassPathResource(profilepath).getFile();
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			props.load(in);
			String value = props.getProperty(key);
			// System.out.println(key +"键的值是："+ value);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据主键key读取主键的值value
	 * 
	 * @param key
	 *            键名
	 */
	public static String readValue(String key) {
		try {
			String value = props.getProperty(key);
			// System.out.println(key +"键的值是："+ value);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 测试代码
	public static void main(String[] args) {
		readValue("appid");
		readValue("/pay.properties", "mch_id");
	}
}

package com.microBusiness.manage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class CodeToSql {
	
	public static void main(String[] args) {
		//读取文件
		List<String> list=toArrayByRandomAccessFile("f:/cccc.txt");
		//写入txt文件
		String outFile="f:/code2.txt";
		String sql="INSERT INTO `t_verification` (`create_date`, `deleted`, `modify_date`, `version`, `batch_no`, `proof_time`, `tag`) VALUES ('2019-03-11 11:15:45', b'0', '2019-03-11 11:15:45', 0, '2', NULL, ";
		for(String str:list) {
			String verCode=str.substring(29,str.length());
			//System.out.println(verCode);
			String hql=sql+"'"+verCode+"');";
			//System.out.println(String.format(sql, verCode));
			//break;
			writeToFile(outFile, hql);
		}
	}

	public static List<String> toArrayByRandomAccessFile(String name) {
		List<String> arrayList = new ArrayList<>();
		try {
			File file = new File(name);
			RandomAccessFile fileR = new RandomAccessFile(file, "r");
			String str = null;
			while ((str = fileR.readLine()) != null) {
				arrayList.add(str);
			}
			fileR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public static void writeToFile(String fileName, String content) {
		try {
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			randomFile.writeBytes(content + "\r\n");
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

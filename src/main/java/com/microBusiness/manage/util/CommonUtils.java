package com.microBusiness.manage.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by mingbai on 2017/4/27.
 * 功能描述：
 * 修改记录：
 */
public class CommonUtils {
    /**
     * 隐藏手机号中间4位
     * @param tel
     * @return
     */
    public static String getHiddenTel(String tel) {
        return tel.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }


    public static String getExportName(String fileName , HttpServletRequest request){
        String agent = request.getHeader("USER-AGENT").toLowerCase();

        try {
            if (agent.contains("firefox")) {
                fileName = new String(fileName.getBytes(), "ISO8859-1");

            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "" ;
        }

        return fileName ;

    }
    
    // 验证特殊符号
    public static boolean strTest(String str){
    	String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean snTest(String str) {
		String regEx = "[0-9a-zA-Z_-]{1,100}";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
    
    public static boolean snTestSpace(String str) {
		String regEx = "[0-9a-zA-Z_-]{1,100}\\s";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean orderTest(String str) {
		String regEx = "\\([0-9]\\)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean fileNameTest(String str) {
		String regEx = "[0-9a-zA-Z_-]{1,100}\\([0-9]\\)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public static boolean fileNameTestSpace(String str) {
		String regEx = "[0-9a-zA-Z_-]{1,100}\\s\\([0-9]\\)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	 public static String getCellValueStr(Cell cell) {
	        String cellValue = "";
	        if (null == cell) {
	            return cellValue;
	        }
	        switch (cell.getCellType()) {
	            case Cell.CELL_TYPE_NUMERIC: // 数字
	                DecimalFormat df = new DecimalFormat("0");
	                cellValue = df.format(cell.getNumericCellValue());
	                break;

	            case Cell.CELL_TYPE_STRING: // 字符串
	                cellValue = cell.getStringCellValue();
	                break;

	            case Cell.CELL_TYPE_BOOLEAN: // Boolean
	                cellValue = cell.getBooleanCellValue() + "";
	                break;

	            case Cell.CELL_TYPE_FORMULA: // 公式
	                cellValue = cell.getCellFormula() + "";
	                break;

	            case Cell.CELL_TYPE_BLANK: // 空值
	                cellValue = "";
	                break;

	            case Cell.CELL_TYPE_ERROR: // 故障
	                cellValue = "";
	                break;

	            default:
	                cellValue = "";
	                break;
	        }
	        return cellValue;
	 }
	 
	 public static String getCellValue(Cell cell) {
	        String cellValue = "";
	        if (null == cell) {
	            return cellValue;
	        }
	        switch (cell.getCellType()) {
	            case Cell.CELL_TYPE_NUMERIC: // 数字
	                DecimalFormat df = new DecimalFormat("0.00");
	                cellValue = df.format(cell.getNumericCellValue());
	                break;

	            case Cell.CELL_TYPE_STRING: // 字符串
	                cellValue = cell.getStringCellValue();
	                break;

	            case Cell.CELL_TYPE_BOOLEAN: // Boolean
	                cellValue = cell.getBooleanCellValue() + "";
	                break;

	            case Cell.CELL_TYPE_FORMULA: // 公式
	                cellValue = cell.getCellFormula() + "";
	                break;

	            case Cell.CELL_TYPE_BLANK: // 空值
	                cellValue = "";
	                break;

	            case Cell.CELL_TYPE_ERROR: // 故障
	                cellValue = "";
	                break;

	            default:
	                cellValue = "";
	                break;
	        }
	        return cellValue;
	    }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		SmsClient client=new SmsClient();
//		String opt = RandomStringUtils.randomNumeric(6);
//		String result_mt = client.mdsmssend("13512565697", "hello", "", "", "", "");
//		System.out.println(123);
		
		getInnerCheckCode("23266233745");
	}
	
	/**
	 * 手机号 (第一位+后五位)乘201812306,取前六位
	 * @param mobile
	 * @return
	 */
	public static String getInnerCheckCode(String mobile){
		int num1 = Integer.valueOf(mobile.substring(0, 1));
		int num2 = Integer.valueOf(mobile.substring(6));
		BigDecimal nu = new BigDecimal(num1+num2).multiply(new BigDecimal(201812306));
		System.out.println(nu);
		String code = String.valueOf(nu).substring(0,6);
		System.out.println(code);
		return code;
	}
}

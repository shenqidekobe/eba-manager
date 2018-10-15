package com.microBusiness.manage.util;

import java.util.regex.Pattern;

/**
 * Created by mingbai on 2017/7/12.
 * 功能描述：
 * 修改记录：
 */
public final class ValidatorUtils {
    private static String TEL_REG = "^(1)\\d{10}$" ;
    private static String TEL_REG2 = "^(1|2)\\d{10}$" ;
    public static boolean isTel(String tel){
        return Pattern.matches(TEL_REG, tel);
    }
    
    public static boolean isTelWithSys(String tel){
        return Pattern.matches(TEL_REG2, tel);
    }
}

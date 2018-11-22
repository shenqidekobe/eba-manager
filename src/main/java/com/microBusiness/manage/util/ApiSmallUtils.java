/*
 * Copyright 2005-2015 dreamforyou. All rights reserved.
 * Support: http://www.dreamforyou
 * License: http://www.dreamforyou/license
 */
package com.microBusiness.manage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import com.microBusiness.manage.entity.ChildMember;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.shiro.codec.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ApiSmallUtils {

	 private static Logger LOGGER = LoggerFactory.getLogger(ApiSmallUtils.class);
	
//	public static String getAccessToken(Long supplierId, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		//URL
//        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+Constant.SMALL_APPID+"&secret="+Constant.SMALL_SECRET;
//        //发送请求
//        String data = getResponse(requestUrl);
//        //解析相应内容（转换成json对象）
//        JSONObject  jsonOld = JSONObject.fromObject(data);
//        //获取access_token
//        String accessToken =String.valueOf(jsonOld.get("access_token"));
//
//        return accessToken;
//        
//    }

	public static Map<Object, Object> getInfo(String code){
	   String url ="https://api.weixin.qq.com/sns/jscode2session?appid="+Constant.SMALL_APPID+"&secret="+Constant.SMALL_SECRET+"&js_code="+code+"&grant_type="+Constant.GRANT_TYPE;

	   //发送请求
       String data = getResponse(url);

       if (data.indexOf("errcode") != -1) {
    	   return null;
	   }

       //解析相应内容（转换成json对象）
       JSONObject json = JSONObject.fromObject(data);
       String sessionKey = json.get("session_key") == null ? null : String.valueOf(json.get("session_key"));
       String openId = json.get("openid") == null ? null : String.valueOf(json.get("openid"));
       if (sessionKey == null) {
    	   return null;
	   }
	   Map<Object, Object> map = new HashMap<Object, Object>();
	   map.put("sessionKey", sessionKey);
	   map.put("openId", openId);

	   return map;
	}
	
	public static Map<Object, Object> getAssInfo(String code, String assKey, String assSecret){
		   String url ="https://api.weixin.qq.com/sns/jscode2session?appid="+assKey+"&secret="+assSecret+"&js_code="+code+"&grant_type="+Constant.GRANT_TYPE;
		   //发送请求
	       String data = getResponse(url);

	       if (data.indexOf("errcode") != -1) {
	    	   return null;
		   }

	       //解析相应内容（转换成json对象）
	       JSONObject json = JSONObject.fromObject(data);
	       
	       String sessionKey = json.get("session_key") == null ? null : String.valueOf(json.get("session_key"));
	       String openId = json.get("openid") == null ? null : String.valueOf(json.get("openid"));

	       if (sessionKey == null) {
	    	   return null;
		   }

		Map<Object, Object> map = new HashMap<Object, Object>();
		   map.put("sessionKey", sessionKey);
		   map.put("openId", openId);

		   return map;
		}
	
	public static CloseableHttpResponse getInputStream(String path, String accessToken, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String url ="https://api.weixin.qq.com/wxa/getwxacode?access_token="+accessToken;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("path", path);
        map.put("width", "430");
        String json = JsonUtils.toJson(map);
        
        CloseableHttpResponse result = WebUtils.httpPostWithJSON(url, json);

        return result;
    }


	public static ChildMember getUserInfo(String encryptedData, String iv, String sessionKey){
		// 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        ChildMember childMember = null;

        try {
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String data = new String(resultByte, "UTF-8");
          
                LOGGER.info("小程序解析用户信息encryptedData: "+data);
                
                JSONObject json = JSONObject.fromObject(data);

                String unionId = String.valueOf(json.get("unionId"));
                String openId = String.valueOf(json.get("openId"));
                String nickName = String.valueOf(json.get("nickName"));
                String avatarUrl = String.valueOf(json.get("avatarUrl"));

                childMember = new ChildMember();
                childMember.setSmOpenId(openId);
                childMember.setUnionId(unionId);
                childMember.setNickName(nickName);
                childMember.setHeadImgUrl(avatarUrl);
                
                LOGGER.info("小程序获取用户信息childMember: "+childMember.toString());
                
                return childMember;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return childMember;
	}
	
	private static String getResponse(String serverUrl){  
		LOGGER.info("小程序获取sessionKey serverUrl :{}" , serverUrl);

        StringBuffer result = new StringBuffer();
        try {  
            URL url = new URL(serverUrl);  
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
  
            String line;  
            while((line = in.readLine()) != null){  
                result.append(line);  
            }  
            in.close();  
  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        LOGGER.info("小程序获取sessionKey result :{}" , result.toString());
        return result.toString();  
    }

}
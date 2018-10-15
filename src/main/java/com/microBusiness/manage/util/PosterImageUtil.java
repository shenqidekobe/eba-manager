package com.microBusiness.manage.util;

import java.awt.Image;
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.fit.cssbox.demo.ImageRenderer;
import org.xml.sax.SAXException;

import com.microBusiness.manage.Setting;

public class PosterImageUtil {

	public static Map<String, Object> genImage(Long id,String unionId){
		Map<String, Object> map=new HashMap<>();
		ImageRenderer render = new ImageRenderer();
		Setting setting = SystemUtils.getSetting();  
		//render.setWindowSize(new Dimension(80, 100), false);
        String url = setting.getFileSystemUrl()+"/ass/common/poster.jhtml?id="+id;
//		String url = "http://192.168.22.119:8088/ass/common/poster.jhtml?id="+id;
        String path = setting.getPosterUploadPath()+ unionId+".png";
        String filePath="/usr/b2b"+path;
    	File srcFile = new File(filePath);
        FileOutputStream out = null;
        String fileUrl="";
        try {
            out = new FileOutputStream(srcFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
        	
            render.renderURL(url, out, ImageRenderer.Type.PNG);
            out.close();
            
            fileUrl=setting.getFileSystemUrl()+path;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } 
        map.put("url", fileUrl);
        try {
        	Image image=ImageIO.read(new File(filePath));
			ImageSizeUtil.Tosmallerpic("/usr/b2b"+setting.getPosterUploadPath(), "/usr/b2b"+setting.getPosterUploadPath(), unionId+".png", image.getWidth(null)/3*2, image.getHeight(null)/3*2, 1);
			map.put("width", image.getWidth(null));
			map.put("height", image.getHeight(null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return map;
	}
}

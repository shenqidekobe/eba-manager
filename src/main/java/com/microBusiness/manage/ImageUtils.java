package com.microBusiness.manage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageUtils {

	 /**  
     * 实现图像的等比缩放  
     * @param source  
     * @param targetW  
     * @param targetH  
     * @return  
     */  
    private static BufferedImage resize(BufferedImage source, int targetW,   
            int targetH) {   
        // targetW，targetH分别表示目标长和宽   
        int type = source.getType();
        BufferedImage target = null;   
        double sx = (double) targetW / source.getWidth();   
        double sy = (double) targetH / source.getHeight();   
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放   
        // 则将下面的if else语句注释即可   
        if (sx < sy) {   
            sx = sy;   
            targetW = (int) (sx * source.getWidth());   
        } else {   
            sy = sx;   
            targetH = (int) (sy * source.getHeight());   
        }   
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade   
            ColorModel cm = source.getColorModel();   
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW,   
                    targetH);   
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();   
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);   
        } else  
            target = new BufferedImage(targetW, targetH, type);   
        Graphics2D g = target.createGraphics();   
        // smoother than exlax:   
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);   
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));   
        g.dispose();   
        return target;   
    }   
  
    /**  
     * 实现图像的等比缩放和缩放后的截取  
     * @param inFilePath 要截取文件的路径  
     * @param outFilePath 截取后输出的路径  
     * @param width 要截取宽度  
     * @param hight 要截取的高度  
     * @param proportion  
     * @throws Exception  
     */  
       
    public static String saveImageAsJpg(String inFilePath, String outFilePath,   
            int width, int height, boolean proportion)throws Exception {   
         File file = new File(inFilePath);   
         InputStream in = new FileInputStream(file);   
         File saveFile = new File(outFilePath);   
  
        BufferedImage srcImage = ImageIO.read(in);   
        if (width > 0 || height > 0) {   
            // 原图的大小   
            int sw = srcImage.getWidth();   
            int sh = srcImage.getHeight(); 
//            if (sw >= width && sh >= height) {
//                srcImage = resize(srcImage, width, height);   
//            }else if (sw >= width && sh < height) {
//            	height = sh;
//            	width = height * sw/sh;
//                srcImage = resize(srcImage, width, height);   
//            }else  if (sw < width && sh >= height) {
//            	width = sw;
//            	height = width * sh/sw;
//                srcImage = resize(srcImage, width, height);   
//            } else if (sw < width && sh < height){   
//                String fileName = saveFile.getName();   
//                String formatName = fileName.substring(fileName   
//                        .lastIndexOf('.') + 1);   
//                ImageIO.write(srcImage, formatName, saveFile);   
//                return;   
//            }   
           
            
            // 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去   
            if (sw > width && sh > height) {
                srcImage = resize(srcImage, width, height);   
            } else {   
                String fileName = saveFile.getName();   
                String formatName = fileName.substring(fileName   
                        .lastIndexOf('.') + 1);   
                ImageIO.write(srcImage, formatName, saveFile);   
                return srcImage.getWidth() + "x" + srcImage.getHeight();   
            }   
        }   
        // 缩放后的图像的宽和高   
        int w = srcImage.getWidth();   
        int h = srcImage.getHeight();
        
        // 如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取   
        if (w == width) {   
            // 计算X轴坐标   
            int x = 0;   
            int y = h / 2 - height / 2;   
            saveSubImage(srcImage, new Rectangle(x, y, width, height), saveFile);   
        }   
        // 否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取   
        else if (h == height) {   
            // 计算X轴坐标   
            int x = w / 2 - width / 2;   
            int y = 0;   
            saveSubImage(srcImage, new Rectangle(x, y, width, height), saveFile);   
        }   
        in.close();
        
        
        //InputStream ins = new FileInputStream(saveFile);   
        //BufferedImage destImage = ImageIO.read(ins);
        return srcImage.getWidth() + "x" + srcImage.getHeight();   
    }   
    /**  
     * 实现缩放后的截图  
     * @param image 缩放后的图像  
     * @param subImageBounds 要截取的子图的范围  
     * @param subImageFile 要保存的文件  
     * @throws IOException  
     */  
    private static void saveSubImage(BufferedImage image,   
            Rectangle subImageBounds, File subImageFile) throws IOException {   
        if (subImageBounds.x < 0 || subImageBounds.y < 0  
                || subImageBounds.width - subImageBounds.x > image.getWidth()   
                || subImageBounds.height - subImageBounds.y > image.getHeight()) {   
            System.out.println("Bad   subimage   bounds");   
            return;   
        }   
        BufferedImage subImage = image.getSubimage(subImageBounds.x,subImageBounds.y, subImageBounds.width, subImageBounds.height);   
        String fileName = subImageFile.getName();   
        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);   
        ImageIO.write(subImage, formatName, subImageFile);   
    }   
    
    
    public static String conbinePics(String rootPath, List<String> picPathList){//纵向处理图片  
        try {
        	int standardWidth = 0;
        	int totalHeight = 0;
        	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        	for (int i = 0; i < picPathList.size(); i++) {
        		String path =  picPathList.get(i);
        		String fileName = path.substring(path.lastIndexOf(File.separator)+1);
        		String inFilePath = rootPath + File.separator + fileName;
        		File fileOne = new File(inFilePath);  
                BufferedImage image = ImageIO.read(fileOne);  
                int width = image.getWidth();// 图片宽度  
                if(width > standardWidth){
                	standardWidth = width;
                }
                int height = image.getHeight();// 图片高度  
                totalHeight += height;
                
                int[] imageArray = new int[width * height];// 从图片中读取RGB  
                imageArray = image.getRGB(0, 0, width, height, imageArray, 0, width);
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("width", width);
                map.put("height", height);
                map.put("imageArray", imageArray);
                list.add(map);
                
			}
        	  // 生成新图片   
            BufferedImage imageResult = new BufferedImage(standardWidth, totalHeight + (picPathList.size() * 5), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) imageResult.getGraphics();
            g2.setBackground(Color.WHITE);//设置背景色
            g2.clearRect(0, 0, standardWidth, totalHeight + (picPathList.size() * 5));//通过使用当前绘图表面的背景色进行填充来清除指定的矩形。
            g2.dispose();
            int lastPicHeight = 0;
            int j = 0;
            for (Map<String, Object> map : list) {
            	Integer imgWidth = (Integer)map.get("width");
            	Integer imgHeight = (Integer)map.get("height");
            	int[] imageArray = (int[])map.get("imageArray");
            	if(j != 0){
            		lastPicHeight = lastPicHeight+5;
            	}
            	j++;
            	imageResult.setRGB(0, lastPicHeight, imgWidth, imgHeight, imageArray, 0, imgWidth);// 设置左半部分的RGB
            	lastPicHeight = lastPicHeight + imgHeight;
			}
            String conbineFileName = "conbine" + new Date().getTime() + "" + new Random().nextInt(10000) + ".jpg";
        	File outFile = new File(rootPath + File.separator + conbineFileName);  
            ImageIO.write(imageResult, "jpg", outFile);// 写图片
            return conbineFileName;
        } catch (Exception e) {  
            e.printStackTrace();
            return "";
        }  
    }
  
    public static void main(String[] args) {
    	String rootPath = "D:\\broadcast\\2014\\6\\29\\3";
    	List<String> picPathList = new ArrayList<String>();
    	picPathList.add("1404053749135773.jpg");
    	picPathList.add("1404053749262671.jpg");
    	picPathList.add("1404053749332809.jpg");
    	picPathList.add("1404054489240182.jpg");
    	picPathList.add("1404054842703132.jpg");
    	conbinePics(rootPath, picPathList); 
    } 
  
}

package com.microBusiness.manage.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import net.sf.json.JSONObject;

/**
 * 生成二维码图片
 */
public class QRCodeUtil {

	public static void main(String[] args) {

	}

	/**
	 * 生成图像
	 * 
	 * @throws WriterException
	 * @throws IOException
	 */
	public static BufferedImage encode(int width, int height, String format, String code_url)
			throws WriterException, IOException {

		String content = code_url;// 内容
		// int width = 200; // 图像宽度
		// int height = 200; // 图像高度
		// String format = "png";// 图像类型
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
		BufferedImage bufferImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
		return bufferImage;
	}


	/**
	 * 解析图像
	 */
	public static void decode(String filePath, String fileName) throws IOException, NotFoundException {
		String fileFullPath = filePath + fileName;// "D://zxing.png";
		BufferedImage image;
		image = ImageIO.read(new File(fileFullPath));
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);
		BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
		Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
		JSONObject content = JSONObject.fromObject(result.getText());
		System.out.println("二维码图片中内容： " + content.toString());

	}

}

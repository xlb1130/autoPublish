package org.xlb.publish.util;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * 
 * 文字图片生成类
 * @author Linbo Xie
 * @since 2015 03 15
 * @version V1.1
 *
 */
public class ImageToAscii {
	private final static char[] asc = { ' ', '`', '.', '^', ',', ':', '~', '"', '<', '!', 'c', 't', '+', '{', 'i', '7',  
        '?', 'u', '3', '0', 'p', 'w', '4', 'A', '8', 'D', 'X', '%', '#', 'H', 'W', 'M' };  
  
	private static StringBuilder imageToAscii(BufferedImage image,int  change) throws IOException {  
	    StringBuilder sb = new StringBuilder();  
	    if (change == 0){
	    	change = 1;
	    }
	    int width = image.getWidth()/change;  
	    int height = image.getHeight()/change;  
	    for (int i = 0; i < height; i++) {  
	        for (int j = 0; j < width; j++) {  
	            int rgb = image.getRGB(j*change, i*change);  
	            int R = (rgb & 0xff0000) >> 16;  
	            int G = (rgb & 0x00ff00) >> 8;  
	            int B = rgb & 0x0000ff;  
	            int gray = (R * 30 + G * 59 + B * 11 + 50) / 100;  
	            int index = 31 * gray / 255;  
	            //反色
	            sb.append(asc[asc.length-index-1]);  
	           // sb.append(asc[index]);  
	        }  
	        sb.append("\n");  
	    }  
	    return sb;  
	}  
	
	 public static InputStream getResource() throws IOException{  
	        //返回读取指定资源的输入流  
	        return ImageToAscii.class.getResourceAsStream("/test.jpg");   
	    }  
	
	public static void printImage() throws FileNotFoundException, IOException{		
		//String imgPath="./test.jpg";
		BufferedImage image = ImageIO.read(getResource());
		System.out.println(imageToAscii(image,3));
	}
}

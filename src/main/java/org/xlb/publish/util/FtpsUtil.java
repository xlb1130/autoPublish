package org.xlb.publish.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * 
 * FTP工具类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class FtpsUtil {
	public static boolean uploadFile(String url,String username, String password, String path, String filename, InputStream input) { 
		if (!path.endsWith("/")) {
			path = path + "/";
		    }
	    boolean success = false; 
	   FTPClient ftp = new FTPClient(); 
	   // FTPSClient client = new FTPSClient("SSL");
	    try { 
	        int reply; 
	        ftp.connect(url,22);
	        ftp.login(username, password);//��¼ 
	        reply = ftp.getReplyCode(); 
	        if (!FTPReply.isPositiveCompletion(reply)) { 
	            ftp.disconnect(); 
	            return success; 
	        } 
	        ftp.changeWorkingDirectory(path); 
	        ftp.storeFile(filename, input);          
	         
	        input.close(); 
	        ftp.logout(); 
	        success = true; 
	    } catch (IOException e) { 
	        e.printStackTrace(); 
	    } finally { 
	        if (ftp.isConnected()) { 
	            try { 
	                ftp.disconnect(); 
	            } catch (IOException ioe) { 
	            } 
	        } 
	    } 
	    return success; 
	}
}

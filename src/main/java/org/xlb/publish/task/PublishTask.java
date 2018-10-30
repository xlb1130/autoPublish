package org.xlb.publish.task;

import java.io.File;
import java.io.FileInputStream;

import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.util.DateUtil;
import org.xlb.publish.util.SFtpUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;

/**
 * 
 * 任务发布执行类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class PublishTask implements Runnable {

	private PublishBean publishBean;
	
	public PublishTask(PublishBean publishBean) {
		this.publishBean = publishBean;
	}
	
	public PublishTask(){
		
	}
	
//	private void doUpload(File file, ChannelSftp sftp,String path) throws Exception{
//		if(file.isDirectory()){
//			try{
//				sftp.mkdir(path+file.getName());
//			}catch(Exception e){
//			}
//			sftp.cd(path+file.getName());
//			File[] fileList = file.listFiles();
//			for(int i=0; i<fileList.length; i++){
//				doUpload((File) fileList[i], sftp,path+file.getName()+"/");
//			}
//			sftp.cd(path);
//		}else{
//			System.out.println("SRC-PATH: "+path+file.getName());
//			System.out.println("LOCAL-PATH: "+file.getAbsolutePath());
//			sftp.put(new FileInputStream(file), file.getName(),ChannelSftp.OVERWRITE);
//			return;
//		}
//	}

	public void run() {
		SFtpUtil ftplist = null;
		try {
			ftplist = new SFtpUtil(publishBean.getIp(), publishBean.getUsername(), publishBean.getPassword());
		} catch (JSchException e) {
			System.out.println("Publish "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+publishBean.getProject().getName()+" ...................Get FTP Instanse Failed!");
			e.printStackTrace();
			return;
		}
		
		try {			
			//备份工程
			ftplist.startRemoteServer("cd  "+publishBean.getPublish().getRemote()+"; tar -cvf  "
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+"-"+DateUtil.getCurrentDate("yyyyMMddhhmmss")+".tar  " 
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4));
			//备份 war包
//			ftplist.startRemoteServer("cd  "+publishBean.getRemote()+";move  "
//					+publishBean.getProcName()
//					+"  "+publishBean.getProcName().split(".")[0]+"_"
//					+DateUtil.getCurrentDate("yyyyMMddhhmmss")
//					+publishBean.getProcName().split(".")[1]);
			System.out.println("Tar "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+publishBean.getPublish().getRemote()
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+" ...................Complete!");
		}catch (Exception e) {
			if (ftplist != null){
				ftplist.close();
			}
			System.out.println("Tar "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+publishBean.getPublish().getRemote()
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+" ...................Fialed!");
			e.printStackTrace();
			return;
		}
		
		//上传
		ChannelSftp sftp = null;
		try {
			sftp = ftplist.getSftpInstance();
			File file = new File(publishBean.getProject().getPath()+publishBean.getProject().getName());
			sftp.cd(publishBean.getPublish().getRemote());
			//之前是先手动解压后上传，改为直接上传包，然后解压
			//this.doUpload(file, sftp,publishBean.getRemote());		
			sftp.put(new FileInputStream(file), file.getName(),ChannelSftp.OVERWRITE);
			System.out.println("Upload "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+publishBean.getPublish().getRemote()
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+" ...................Complete!");
		} catch (Exception e) {
			System.out.println("Upload "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+publishBean.getPublish().getRemote()
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+" ...................Failed!");
			e.printStackTrace();
			if (sftp != null){
				sftp.disconnect();
			}
			if (ftplist != null){
				ftplist.close();
			}
			return;
		}
		
		String command  = "";
		//解压
		try {	
			//尝试创建文件夹，应对第一次发布的情况
			try{
				sftp.mkdir(publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4));
			}catch(Exception e){				
				e.printStackTrace();
			}
			
			command = publishBean.getPublish().getUnpack().replace(
					"SRCNAME", publishBean.getProject().getName());
			command = command.replace("DESTNAME",publishBean.getProject().getName()
					.substring(0,publishBean.getProject().getName().length()-4));
			ftplist.startRemoteServer("cd  "+publishBean.getPublish().getRemote()+";"+command);
			System.out.println("Upack "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+" With command : "+command
					+" ...................Complete! ");
		}catch (Exception e) {
			if (sftp != null){
				sftp.disconnect();
			}
			if (ftplist != null){
				ftplist.close();
			}
			System.out.println("Upack "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
					+" With command : "+command
					+" ...................Failed! ");
			e.printStackTrace();
		}
		
		try{
			if (sftp != null){
				sftp.disconnect();
			}
			if (ftplist != null){
				ftplist.close();
			}
		}catch(Exception e ){
			
		}
		
		return;
	}

}

package org.xlb.publish.task;

import java.io.File;
import java.io.FileInputStream;

import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.svn.tools.ConsoleLog;
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
public class PublishTask extends ConsoleLog implements Runnable {

	private PublishBean publishBean;
	
	public PublishTask(PublishBean publishBean) {
		this.publishBean = publishBean;
	}
	
	public PublishTask(){
		super.log = true;
	}
	
	private void doUpload(File file, ChannelSftp sftp,String path) throws Exception{
		super.log("开始发布……");
		if(file.isDirectory()){
			try{
				sftp.mkdir(path+file.getName());
			}catch(Exception e){
			}
			sftp.cd(path+file.getName());
			File[] fileList = file.listFiles();
			for(int i=0; i<fileList.length; i++){
				doUpload(fileList[i], sftp,path+file.getName()+"/");
			}
			sftp.cd(path);
		}else{
//			System.out.println("SRC-PATH: "+path+file.getName());
//			System.out.println("LOCAL-PATH: "+file.getAbsolutePath());
			sftp.put(new FileInputStream(file), file.getName(),ChannelSftp.OVERWRITE);
			return;
		}
		super.log("发布成功");
	}

	public void run() {
		SFtpUtil ftpList;
		try {
			ftpList = new SFtpUtil(publishBean.getIp(), publishBean.getUsername(), publishBean.getPassword());
		} catch (JSchException e) {
			super.log("远程主机连接失败");
			e.printStackTrace();
			return;
		}
		
		try {			
			//备份工程
			ftpList.startRemoteServer("cd  "+publishBean.getPublish().getRemote()+"; tar -cvf  "
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4)
					+"-"+DateUtil.getCurrentDate("yyyyMMddhhmmss")+".tar  " 
					+publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4));
			//备份 war包
//			ftpList.startRemoteServer("cd  "+publishBean.getRemote()+";move  "
//					+publishBean.getProcName()
//					+"  "+publishBean.getProcName().split(".")[0]+"_"
//					+DateUtil.getCurrentDate("yyyyMMddhhmmss")
//					+publishBean.getProcName().split(".")[1]);
			super.log("备份成功");
		}catch (Exception e) {
			if (ftpList != null){
				ftpList.close();
			}
			super.log("备份失败");
			e.printStackTrace();
			return;
		}
		
		//上传
		ChannelSftp sftp = null;
		try {
			sftp = ftpList.getSftpInstance();
			File file = new File(publishBean.getProject().getPath()+publishBean.getProject().getName());
			sftp.cd(publishBean.getPublish().getRemote());
			//之前是先手动解压后上传，改为直接上传包，然后解压
			this.doUpload(file, sftp,publishBean.getPublish().getRemote());
//			sftp.put(new FileInputStream(file), file.getName(),ChannelSftp.OVERWRITE);
		} catch (Exception e) {
			super.log("发布失败");
			e.printStackTrace();
			if (sftp != null){
				sftp.disconnect();
			}
			if (ftpList != null){
				ftpList.close();
			}
			return;
		}
		
		String command  = publishBean.getPublish().getUnpack();
		if(command != null && !"".equals(command)){
			//解压
			try {
				//尝试创建文件夹，应对第一次发布的情况
				try{
					sftp.mkdir(publishBean.getProject().getName().substring(0,publishBean.getProject().getName().length()-4));
				}catch(Exception e){
					e.printStackTrace();
				}
				command = command.replace(
						"SRCNAME", publishBean.getProject().getName());
				command = command.replace("DESTNAME",publishBean.getProject().getName()
						.substring(0,publishBean.getProject().getName().length()-4));
				ftpList.startRemoteServer("cd  "+publishBean.getPublish().getRemote()+";"+command);
				super.log("解压成功");
			}catch (Exception e) {
				if (sftp != null){
					sftp.disconnect();
				}
				if (ftpList != null){
					ftpList.close();
				}
				super.log("失败");
			}

		}
		try{
			if (sftp != null){
				sftp.disconnect();
			}
			if (ftpList != null){
				ftpList.close();
			}
		}catch(Exception e ){}
		return;
	}

}

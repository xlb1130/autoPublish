package org.xlb.publish.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * 
 * SFTP工具类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class SFtpUtil {
	
	private JSch jsch;
	
	private  ChannelSftp sftp ;
	
	private Session sshSession;
	
	private Channel channel;
    
    public SFtpUtil(String url,String username, String password) throws JSchException{
    	 jsch = new JSch();
         sshSession = jsch.getSession(username, url, 22);
         sshSession.setPassword(password);
         Properties sshConfig = new Properties();
         sshConfig.put("StrictHostKeyChecking", "no");
         sshConfig.put("kex", "diffie-hellman-group1-sha1,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha1,diffie-hellman-group-exchange-sha256");
         sshSession.setConfig(sshConfig);
         sshSession.connect();
    }
    
    public ChannelSftp getSftpInstance() throws JSchException{
	  channel = sshSession.openChannel("sftp");
      channel.connect();
      sftp = (ChannelSftp) channel;
    	return sftp;
    }
    
    public void  startRemoteServer(String command) throws Exception{
	  channel = sshSession.openChannel("exec");
      ((ChannelExec) channel).setCommand(command);
      ((ChannelExec) channel).setErrStream(System.err);

      channel.connect();
      InputStream in = channel.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String buf = null;
      while ((buf = reader.readLine()) != null) {
          System.out.println(buf);
      }
    }
    
    public void upload(String path, String filename, InputStream input) throws SftpException{
    	  sftp.cd(path);
          sftp.put(input, filename);
    }
    
    public void close(){
    	closeChannel(sftp);
        closeChannel(channel);
        closeSession(sshSession);
    }

    private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }
}

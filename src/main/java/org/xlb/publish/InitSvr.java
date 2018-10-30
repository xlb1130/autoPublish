package org.xlb.publish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.config.Config;
import org.xlb.publish.config.element.*;

/**
 * 
 * 配置文件读取类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class InitSvr {
	
	private  Map<String,Host> hostMap = new HashMap<>();
	
	private  Map<String,User> userMap = new HashMap<>();

	private  Map<String,Svn> svnMap = new HashMap<>();
	
	private List<PublishBean> beans = new ArrayList<>();
	
	private String configFile;
	
	private static InitSvr obj = null;
	
	private  static final String CONFIG_FILE = "./config.xml";
	
	private InitSvr(){
		
	}
	
	public static InitSvr getInstance() throws Exception{
		if (obj == null){
			obj = new InitSvr();
			obj.init();
		}
		return obj;
	}
	
	public static InitSvr getInstance(String configFile) throws Exception{
		if (obj == null){
			obj = new InitSvr();
			obj.configFile = configFile;
			obj.init();
		}
		return obj;
	}
	
	private void init() throws Exception{
		Config config = this.getConfig();
		List<User> userList = config.getUsers();
		List<Host> hostList = config.getHosts();
		List<Svn> svnList = config.getSvns();
		List<Project> projects = config.getProjects();
		PublishBean bean;
		for(int i=0; i<userList.size(); i++){
			userMap.put((userList.get(i)).getName(), userList.get(i)) ;
		}
		for(int i=0; i<hostList.size(); i++){
			hostMap.put((hostList.get(i)).getName(), hostList.get(i)) ;
		}
		for(int i=0; i<svnList.size(); i++){
			svnMap.put((svnList.get(i)).getName(), svnList.get(i)) ;
		}
		for(int i=0; i<projects.size(); i++){
			Project p = projects.get(i);
			List<Publish> pubList = p.getPublishs();
			for (int j=0; j<pubList.size(); j++){
				Publish temp = pubList.get(j);
				bean = new PublishBean();
				bean.setIp((hostMap.get(temp.getHost())).getIp());
				bean.setUsername((userMap.get(temp.getUser())).getUsername());
				bean.setPassword((userMap.get(temp.getUser())).getPassword());
				bean.setProject(p);
				bean.setPublish(temp);
				bean.setSvn(svnMap.get(temp.getSvn()));
				beans.add(bean);
			}
		}
	}
	
	private  Config  getConfig() throws Exception{
		Serializer serializer = new Persister();		
		String xmlStr = this.getConfigXML();
		xmlStr = xmlStr.substring(xmlStr.indexOf("<xml>")+5, xmlStr.indexOf("</xml>"));
		return serializer.read(Config.class,xmlStr);
	}
	
	private  Reader getReader() throws FileNotFoundException{
		if (configFile == null || "".equals(configFile)){
			return new FileReader(new File(CONFIG_FILE));
		}
		return new FileReader(new File(configFile));
	}
	
	private  String getConfigXML() throws IOException{
		BufferedReader br  = new BufferedReader(getReader());
		String xml = "";
		String temp;
		while((temp = br.readLine())!= null){
			xml += temp;
		}
		return xml;
	}

	public Map<String, Host> getHostMap() {
		return hostMap;
	}

	public void setHostMap(Map<String, Host> hostMap) {
		this.hostMap = hostMap;
	}

	public Map<String, User> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, User> userMap) {
		this.userMap = userMap;
	}

	public List<PublishBean> getBeans() {
		return beans;
	}

	public void setBeans(List<PublishBean> beans) {
		this.beans = beans;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static InitSvr getObj() {
		return obj;
	}

	public static void setObj(InitSvr obj) {
		InitSvr.obj = obj;
	}

}

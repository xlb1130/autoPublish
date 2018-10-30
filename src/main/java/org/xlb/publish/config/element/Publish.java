package org.xlb.publish.config.element;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * @author Linbo Xie
 * @since 2015 03 15
 * @version V1.1
 *
 */
@Root
public class Publish {

	@Element
	private String host;
	
	@Element
	private String user;

	@Element
	private String svn;

	@Element
	private String fileType;

	@Element
	private String libPath;

	@Element
	private String srcPath;

	@Element
	private String mainClass;

	@Element
	private String resourcePath;

	@Element
	private String compileType;

	@Element
	private String webPath;
	
	@Element
	private String remote;
	
	@Element
	private String command;
	
	@Element
	private String index;
	
	@Element
	private String unpack;
	
	@Element
	private String sleep;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getUnpack() {
		return unpack;
	}

	public void setUnpack(String unpack) {
		this.unpack = unpack;
	}

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getSvn() {
		return svn;
	}

	public void setSvn(String svn) {
		this.svn = svn;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getWebPath() {
		return webPath;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	public String getLibPath() {
		return libPath;
	}

	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getCompileType() {
		return compileType;
	}

	public void setCompileType(String compileType) {
		this.compileType = compileType;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}
}

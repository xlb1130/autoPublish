package org.xlb.publish.bean;

import org.xlb.publish.config.element.Project;
import org.xlb.publish.config.element.Publish;
import org.xlb.publish.config.element.Svn;

/**
 * 
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class PublishBean {
	private String ip;
	private String username;
	private String password;
	private Project project;
	private Publish publish;
	private Svn svn;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Publish getPublish() {
		return publish;
	}

	public void setPublish(Publish publish) {
		this.publish = publish;
	}

	public Svn getSvn() {
		return svn;
	}

	public void setSvn(Svn svn) {
		this.svn = svn;
	}
}

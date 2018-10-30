package org.xlb.publish.config;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.xlb.publish.config.element.Host;
import org.xlb.publish.config.element.Project;
import org.xlb.publish.config.element.Svn;
import org.xlb.publish.config.element.User;

/**
 * 
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
@Root
public class Config {
	
	@ElementList
	private List<Host>  hosts;
	
	@ElementList
	private List<User> users;

	@ElementList
	private List<Svn> svns;
	
	@ElementList
	private List<Project> projects;

	public List<Host> getHosts() {
		return hosts;
	}

	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Svn> getSvns() {
		return svns;
	}

	public void setSvns(List<Svn> svns) {
		this.svns = svns;
	}
}

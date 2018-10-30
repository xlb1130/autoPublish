package org.xlb.publish.config.element;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
@Root
public class User {
	
	@Attribute
	private String name;
	
	@Element
	private String username;
	
	@Element
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
}

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
public class Host {

	@Attribute
	private String name;
	
	@Element
	private String ip;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}

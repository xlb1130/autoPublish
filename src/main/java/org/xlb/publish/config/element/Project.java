package org.xlb.publish.config.element;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
@Root
public class Project {
	
	@Attribute
	private String name;
	
	@Attribute
	private String path;
	
	@ElementList(inline=true)
	private List<Publish> publish;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Publish> getPublishs() {
		return publish;
	}

	public void setPublishs(List<Publish> publishs) {
		this.publish = publishs;
	}

}

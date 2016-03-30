package net.miwashi.jsonapi;

import java.net.MalformedURLException;
import java.net.URL;

import net.miwashi.teststatus.model.Requirement;

public class URLRequirement extends SimpleRequirement{

	public URLRequirement(long id, String name) {
		super(id, name);
	}
	
	public URLRequirement(Requirement requirement) {
		super(requirement);
	}

	public String getHost(){
		String host = "unknown";
		try {
			URL url = new URL(this.getParameter());
			host = url.getHost();
		} catch (MalformedURLException ignore) {}
		return host;
	}
}

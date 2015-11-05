package com.miwashi.model.transients.jsonapi;

import com.miwashi.model.Group;

public class SimpleTeam {
	
	private long id;
	private String name;
	
	public SimpleTeam(Group group) {
		this.id = group.getId();
		this.name = group.getName();
	}
	
	public SimpleTeam() {
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}

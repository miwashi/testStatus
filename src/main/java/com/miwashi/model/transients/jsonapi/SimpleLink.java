package com.miwashi.model.transients.jsonapi;

public class SimpleLink {
	long id;
	String name;
	
	public SimpleLink(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
		
		
}

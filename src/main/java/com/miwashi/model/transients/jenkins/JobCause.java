package com.miwashi.model.transients.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobCause {
	private String shortDescription;
	
	
	
	public String getShortDescription() {
		return shortDescription;
	}



	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}



	public String toString(){
		return new Gson().toJson(this);
	}
}

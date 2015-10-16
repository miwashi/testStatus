package com.miwashi.model.transients.jenkins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobResult {
	private Integer failCount;
	private Integer skipCount;
	private Integer totalCount;
	private String urlName;
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public Integer getSkipCount() {
		return skipCount;
	}
	public void setSkipCount(Integer skipCount) {
		this.skipCount = skipCount;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}
}
